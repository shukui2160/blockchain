package com.minute.application.app.config;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

@Component
@Aspect
public class LoginAspect {

	@Value("${com.tlabs.application.app.secret}")
	private String secret;

	@Pointcut("execution(public * com.minute.application.app.controller.*.*(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object deBefore(ProceedingJoinPoint joinPoint) throws Throwable {
		// 根据RequestContextHolder获取当前请求的HttpServletRequest
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();

		// 必须字段
		String tlabsEnvironment = request.getHeader("tlabsEnvironment");
		if (StrUtil.isBlank(tlabsEnvironment)) {
			// 抛出异常,全局异常处理器中处理
			throw new AuthException("tlabsEnvironment is Invalid !");
		}

		Signature signature = joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();
		RequireLogin requireLogin = method.getAnnotation(RequireLogin.class);

		// 当action标注了@RequireLogin的时候http请求头中必须包含tlabsToken字段
		String tlabsToken = null;
		String token = request.getHeader("tlabsToken");
		if (requireLogin != null) {
			tlabsToken = checkToken(token);
			if (tlabsToken == null) {
				throw new AuthException("tlabsToken is Invalid !");
			}
		} else {
			if (token != null) {
				tlabsToken = checkToken(token);
			}
		}

		// Base64解码
		tlabsEnvironment = Base64.decodeStr(tlabsEnvironment);
		buildContext(tlabsEnvironment, tlabsToken);

		// 执行action的逻辑
		Object result = joinPoint.proceed();

		// 释放ThreadLocal资源
		RequestContext.remove();

		return result;
	}

	// token检测 并 解密
	private String checkToken(String tlabsToken) {
		if (tlabsToken == null) {
			return null;
		}
		try {
			SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, secret.getBytes());
			return aes.decryptStr(tlabsToken);
		} catch (Exception e) {
			return null;
		}
	}

	// 构建RequestContext
	private void buildContext(String tlabsEnvironment, String tlabsToken) {
		TlabsToken token = JSONObject.parseObject(tlabsEnvironment, TlabsToken.class);
		if (tlabsToken != null) {
			TlabsToken tok = JSONObject.parseObject(tlabsToken, TlabsToken.class);
			token.setUserId(tok.getUserId());
			token.setDate(tok.getDate());
			token.setDeviceId(tok.getDeviceId());
		}
		RequestContext.set(token);
	}

}

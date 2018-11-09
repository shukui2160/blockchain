package com.minute.application.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.CommonUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.app.config.RequestContext;
import com.minute.application.app.config.RequireLogin;
import com.minute.application.app.remote.UserRemoteService;
import com.minute.application.app.service.UserService;



//用户相关,注册,登录,忘记密码,更改密码
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRemoteService userRemoteService;

	@Autowired
	private UserService userService;

	// tlabsToken过期检测
	@PostMapping("/expireCheck")
	public TlabsResult expireCheck(@RequestBody JSONObject request) {
		String tlabsToken = request.getString("tlabsToken");
		TlabsResult verifyResult = CommonUtils.verifyNull(tlabsToken);
		if (verifyResult != null) {
			return verifyResult;
		}
		return userService.expireCheck(tlabsToken);
	}

	// 发送邮件(用户注册，忘记密码)
	@PostMapping("/sendEmail")
	public TlabsResult sendEmail4Regist(@RequestBody JSONObject request) {
		String operation = request.getString("operation");
		String email = request.getString("email");
		TlabsResult verifyResult = CommonUtils.verifyNull(operation, email);
		if (verifyResult != null) {
			return verifyResult;
		}
		String languageId = RequestContext.get().getLanguageId();
		return userRemoteService.sendEmail(operation, email, languageId);
	}

	// 邮箱验证码验证(用户注册，忘记密码)
	@PostMapping("/verifyEmailCode")
	public TlabsResult verifyEmailCode(@RequestBody JSONObject request) {
		String operation = request.getString("operation");
		String email = request.getString("email");
		String code = request.getString("code");
		TlabsResult verifyResult = CommonUtils.verifyNull(operation, email, code);
		if (verifyResult != null) {
			return verifyResult;
		}
		String languageId = RequestContext.get().getLanguageId();
		return userRemoteService.verifyEmailCode(operation, email, code, languageId);
	}

	// 用户注册
	@PostMapping("/regist")
	public TlabsResult regist(@RequestBody JSONObject request) {
		String email = request.getString("email");
		String code = request.getString("code");
		String passwd = request.getString("passwd");
		String markedWord = request.getString("markedWord");
		TlabsResult verifyResult = CommonUtils.verifyNull(email, code, passwd, markedWord);
		if (verifyResult != null) {
			return verifyResult;
		}
		String languageId = RequestContext.get().getLanguageId();
		//return userRemoteService.regist(email, code, passwd, markedWord, languageId);
		return userService.regist(email, code, passwd, markedWord, languageId);
	}

	// 用户登录
	@PostMapping("/login")
	public TlabsResult login(@RequestBody JSONObject request) {
		String email = request.getString("email");
		String passwd = request.getString("passwd");
		TlabsResult verifyResult = CommonUtils.verifyNull(email, passwd);
		if (verifyResult != null) {
			return verifyResult;
		}
		String languageId = RequestContext.get().getLanguageId();
		return userService.login(email,passwd,languageId);
	}

	// 更改密码
	@PostMapping("/modifyPasswd")
	@RequireLogin
	public TlabsResult modifyPasswd(@RequestBody JSONObject request) {
		String oldpwd = request.getString("oldpwd");
		String newpwd = request.getString("newpwd");
		TlabsResult verifyResult = CommonUtils.verifyNull(oldpwd, newpwd);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		String languageId = RequestContext.get().getLanguageId();
		return userRemoteService.modifyPasswd(userId, newpwd, oldpwd, languageId);
	}

	// 忘记密码
	@PostMapping("/forgetPasswd")
	public TlabsResult forgetPasswd(@RequestBody JSONObject request) {
		String email = request.getString("email");
		String code = request.getString("code");
		String passwd = request.getString("passwd");
		TlabsResult verifyResult = CommonUtils.verifyNull(email, code, passwd);
		if (verifyResult != null) {
			return verifyResult;
		}
		String languageId = RequestContext.get().getLanguageId();
		return userRemoteService.forgetPasswd(email, code, passwd, languageId);
	}

	// 判断是否存在支付密码
	@RequireLogin
	@PostMapping("/existPayPasswd")
	public TlabsResult existPayPasswd() {
		String userId = RequestContext.get().getUserId();
		return userRemoteService.existPayPasswd(userId);
	}

	// 设置支付密码
	@RequireLogin
	@PostMapping("/initsPayPasswd")
	public TlabsResult initsPayPasswd(@RequestBody JSONObject request) {
		String payPasswd = request.getString("payPasswd");
		String markedWord = request.getString("markedWord");
		TlabsResult verifyResult = CommonUtils.verifyNull(payPasswd);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return userRemoteService.initsPayPasswd(userId, payPasswd, markedWord);
	}
}

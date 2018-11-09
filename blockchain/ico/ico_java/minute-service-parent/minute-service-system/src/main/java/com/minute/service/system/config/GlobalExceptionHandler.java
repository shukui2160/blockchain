package com.minute.service.system.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Value("${spring.application.name}")
	private String name;

	// 声明要捕获的异常
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public TlabsResult defultExcepitonHandler(HttpServletRequest request, Exception e) {
		e.printStackTrace();
		TlabsResult result = ResultUtils.createException();
		result.setMsg(name+"###:" + e.getMessage());
		return result;
	}

}

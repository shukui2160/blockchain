package com.minute.service.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.user.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	// 用户注册-发送邮件
	@PostMapping("/sendEmail")
	public TlabsResult sendEmail(@RequestParam(value = "operation") String operation, @RequestParam String email,
			@RequestParam String languageId) {
		return userService.sendEmail(operation, email, languageId);
	}

	// 用户注册-验证验证码
	@PostMapping("/verifyEmailCode")
	public TlabsResult verifyEmailCode(@RequestParam(value = "operation") String operation, @RequestParam String email,
			@RequestParam String code, @RequestParam String languageId) {
		return userService.verifyEmailCode(operation, email, code, languageId);
	}

	@PostMapping("/regist")
	public TlabsResult regist(@RequestParam(value = "email") String email, @RequestParam(value = "code") String code,
			@RequestParam(value = "passwd") String passwd, @RequestParam(value = "markedWord") String markedWord,
			@RequestParam(value = "languageId") String languageId) {
		return userService.regist(email, code, passwd, markedWord, languageId);
	}

	@PostMapping("/login")
	public TlabsResult login(@RequestParam(value = "email") String email, @RequestParam(value = "passwd") String passwd,
			@RequestParam(value = "languageId") String languageId) {
		return userService.login(email, passwd, languageId);
	}

	@PostMapping("/modifyPasswd")
	public TlabsResult modifyPasswd(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "newpwd") String newpwd, @RequestParam(value = "oldpwd") String oldpwd,
			@RequestParam(value = "languageId") String languageId) {
		return userService.modifyPasswd(userId, newpwd, oldpwd, languageId);
	}

	@PostMapping("/forgetPasswd")
	public TlabsResult forgetPasswd(@RequestParam(value = "email") String email,
			@RequestParam(value = "code") String code, @RequestParam(value = "passwd") String passwd,
			@RequestParam(value = "languageId") String languageId) {
		return userService.forgetPasswd(email, code, passwd, languageId);
	}

	@PostMapping("/existPayPasswd")
	public TlabsResult existPayPasswd(@RequestParam(value = "userId") String userId) {
		return userService.existPayPasswd(userId);
	}

	@PostMapping("/initsPayPasswd")
	public TlabsResult initsPayPasswd(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "payPasswd") String payPasswd, @RequestParam(value = "markedWord") String markedWord) {
		return userService.initsPayPasswd(userId,payPasswd,markedWord);
	}
	
	@PostMapping("/authPayPasswd")
	public TlabsResult authPayPasswd(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "payPasswd") String payPasswd, @RequestParam(value = "languageId") String languageId) {
		return userService.authPayPasswd(userId,payPasswd,languageId);
	}
	
	@PostMapping("/selectEamilById")
	public TlabsResult selectEmailById(@RequestParam(value = "userId") String userId) {
		if(userId!=null) {
			return this.userService.selectEmaliById(Long.valueOf(userId));
		}else {
			return ResultUtils.createException();
		}
		
	}
	
}

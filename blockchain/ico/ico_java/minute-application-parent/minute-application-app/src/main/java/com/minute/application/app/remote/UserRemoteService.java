package com.minute.application.app.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.common.tookit.result.TlabsResult;

@RequestMapping("/user/v1")
@FeignClient(name = "minute-service-user")
public interface UserRemoteService {

	@PostMapping("/user/sendEmail")
	public TlabsResult sendEmail(@RequestParam(value = "operation") String operation,
			@RequestParam(value = "email") String email, @RequestParam(value = "languageId") String languageId);

	@PostMapping("/user/verifyEmailCode")
	public TlabsResult verifyEmailCode(@RequestParam(value = "operation") String operation,
			@RequestParam(value = "email") String email, @RequestParam(value = "code") String code,
			@RequestParam(value = "languageId") String languageId);

	@PostMapping("/user/regist")
	public TlabsResult regist(@RequestParam(value = "email") String email, @RequestParam(value = "code") String code,
			@RequestParam(value = "passwd") String passwd, @RequestParam(value = "markedWord") String markedWord,
			@RequestParam(value = "languageId") String languageId);

	@PostMapping("/user/login")
	public TlabsResult login(@RequestParam(value = "email") String email, @RequestParam(value = "passwd") String passwd,
			@RequestParam(value = "languageId") String languageId);

	@PostMapping("/user/modifyPasswd")
	public TlabsResult modifyPasswd(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "newpwd") String newpwd, @RequestParam(value = "oldpwd") String oldpwd,
			@RequestParam(value = "languageId") String languageId);

	@PostMapping("/user/forgetPasswd")
	public TlabsResult forgetPasswd(@RequestParam(value = "email") String email,
			@RequestParam(value = "code") String code, @RequestParam(value = "passwd") String passwd,
			@RequestParam(value = "languageId") String languageId);

	@PostMapping("/user/existPayPasswd")
	public TlabsResult existPayPasswd(@RequestParam(value = "userId") String userId);

	@PostMapping("/user/initsPayPasswd")
	public TlabsResult initsPayPasswd(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "payPasswd") String payPasswd, @RequestParam(value = "markedWord") String markedWord);

	@PostMapping("/user/authPayPasswd")
	public TlabsResult authPayPasswd(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "payPasswd") String payPasswd, @RequestParam(value = "languageId") String languageId);

}

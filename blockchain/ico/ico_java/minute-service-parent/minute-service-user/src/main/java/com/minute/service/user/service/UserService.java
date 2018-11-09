package com.minute.service.user.service;

import com.common.tookit.result.TlabsResult;

public interface UserService {
	TlabsResult sendEmail(String operation, String email, String languageId);

	TlabsResult verifyEmailCode(String operation, String email, String code, String languageId);

	TlabsResult regist(String email, String code, String passwd, String markedWord, String languageId);

	TlabsResult login(String email, String passwd, String languageId);

	TlabsResult modifyPasswd(String userId, String newpwd, String oldpwd, String languageId);

	TlabsResult forgetPasswd(String email, String code, String passwd, String languageId);

	TlabsResult existPayPasswd(String userId);

	TlabsResult initsPayPasswd(String userId, String payPasswd, String markedWord);

	TlabsResult authPayPasswd(String userId, String payPasswd, String languageId);
	
	TlabsResult selectEmaliById(Long userId);

}

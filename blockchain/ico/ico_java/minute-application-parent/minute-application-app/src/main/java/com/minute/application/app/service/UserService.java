package com.minute.application.app.service;

import com.common.tookit.result.TlabsResult;

public interface UserService {
	
	public TlabsResult expireCheck(String tlabsToken);

	public TlabsResult login(String email, String passwd, String languageId);

	public TlabsResult regist(String email, String code, String passwd, String markedWord, String languageId);
	
	

}

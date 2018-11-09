package com.minute.service.system.service;

import com.common.tookit.result.TlabsResult;

public interface SystemUserService {

	TlabsResult sysLogin(String name, String passwd);
	
	//select name by primary id
	TlabsResult selectNameById(String id);

}

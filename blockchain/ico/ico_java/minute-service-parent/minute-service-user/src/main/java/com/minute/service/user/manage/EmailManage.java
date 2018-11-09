package com.minute.service.user.manage;

public interface EmailManage {

	void sendEmail4Regist(String email, String code);

	void sendEmail4forgetPasswd(String email, String code);



}

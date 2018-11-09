package com.minute.service.user.manage.impl;

import org.springframework.stereotype.Component;

import com.minute.service.user.manage.EmailManage;

@Component
public class EmailManagerImpl implements EmailManage {

	@Override
	public void sendEmail4Regist(String email, String code) {

	}

	@Override
	public void sendEmail4forgetPasswd(String email, String code) {

	}

}

package com.minute.service.user.common;

public enum UserEnum {
	HAVE_REGIST("201", "邮箱已注册!", "email has been registered!"),
	EMAIL_CODE_EXPIRE("202","验证码已过期！","email code is expire!"),
	EMAIL_CODE_ERROR("203","验证码错误！","email code is error!"),
	SESSION_EXPIRE("204","回话失效！","session is expire!"),
	USER_LOCKED("205","用户已锁定,2小时候后重新登录！","the user has been locked up, and 2 when you log in again!"),
	
	ACCOUNT_ERROR("206","用户不存在！","the user does not exist! "),
	PASSWD_ERROR("207","密码错误！","wrong password!! ");
	

	private String code;
	private String chMsg;
	private String enMsg;

	private UserEnum(String code, String cnMsg, String enMsg) {
		this.code = code;
		this.chMsg = cnMsg;
		this.enMsg = enMsg;
	}

	public String getCode() {
		return code;
	}

	public String getChMsg() {
		return chMsg;
	}

	public String getEnMsg() {
		return enMsg;
	}

	public String getMsg(String languageId) {
		if ("1".equals(languageId)) {
			return getChMsg();
		} else {
			return getEnMsg();
		}
	}

}

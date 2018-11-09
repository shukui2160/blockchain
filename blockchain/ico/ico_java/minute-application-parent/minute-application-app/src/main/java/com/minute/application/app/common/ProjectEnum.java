package com.minute.application.app.common;


public enum ProjectEnum {
	TOKEN_EXIT("201", "tokens already exist!", "tokens already exist!"),
	PAY_PASSWD_ERROR("202", "支付密码错误！", "pay password error！"),
	PROJECT_FINISH("203", "项目已经结束！", "project finish!"),
    EXCEED_BALANCE ("204", "代币数量不足！", "token run out!"),
    NO_BALANCE ("205", "账户余额不足！", "account balance run out!"),
    PROJECT_NO_START ("206", "项目未开始！", "project not start!"),
	PROJECT_EXCEPTION ("208", "项目异常！", "project exception!");
	private String code;
	private String chMsg;
	private String enMsg;

	private ProjectEnum(String code, String cnMsg, String enMsg) {
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

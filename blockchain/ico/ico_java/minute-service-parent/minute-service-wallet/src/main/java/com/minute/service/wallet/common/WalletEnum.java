package com.minute.service.wallet.common;


public enum WalletEnum {
	
    NO_BALANCE ("205", "账户余额不足！", "account balance run out!");
    
	private String code;
	private String chMsg;
	private String enMsg;

	private WalletEnum(String code, String cnMsg, String enMsg) {
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

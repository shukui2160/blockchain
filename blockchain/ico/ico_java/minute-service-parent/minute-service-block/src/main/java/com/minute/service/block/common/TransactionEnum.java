package com.minute.service.block.common;


public enum TransactionEnum {
	
    TRANSACTION_FAIL ("201", "转账失败！", "transation fail!");
    
	private String code;
	private String chMsg;
	private String enMsg;

	private TransactionEnum(String code, String cnMsg, String enMsg) {
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

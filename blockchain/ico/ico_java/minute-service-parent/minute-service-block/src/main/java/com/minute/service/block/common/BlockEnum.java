package com.minute.service.block.common;

public enum BlockEnum {
	CONTRACT_ADDRESS_ERROR("201", "无效的合约地址！", "无效的合约地址！"),
	UN_CONFIRM_TRANSACTION("202", "交易未确认！", "交易未确认！");
	private String code;
	private String chMsg;
	private String enMsg;

	private BlockEnum(String code, String cnMsg, String enMsg) {
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

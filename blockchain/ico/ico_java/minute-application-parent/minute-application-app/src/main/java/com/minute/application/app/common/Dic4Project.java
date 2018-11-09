package com.minute.application.app.common;

public enum Dic4Project {
	STATUS_DRAFT("0","草稿状态"),
	STATUS_PENDING("1","待审批"),
	STATUS_APPROVE("2","审批通过"),
	STATUS_REJECT("3","驳回"),
	STATUS_WAITSTART("4","待开始"),
	STATUS_UNDERWAY("5","进行中"),
	STATUS_SYOPINTHEOUGH("6","中途停止"),
	STATUS_SUCCESS("7","成功结算中"),
	STATUS_FAIL("8","失败结算中"),
	STATUS_SUCCESSEND("9","成功结束"),
	STATUS_FAILEND("10","失败结束");
	
	
	
	private Dic4Project(String type, String msg) {
		this.key = type;
		this.value = msg;
	}
	
	
	private String key;
	private String value;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}

package com.tlabs.blockchain.model;

public class ValidateBlock {
	//认证节点
	private Block ValidateBlock;
	//认证状态
	private String status;
	//认证开始时间
	private String StartTime;
	//认证结束时间
	private String EndTime;
	public Block getValidateBlock() {
		return ValidateBlock;
	}
	public void setValidateBlock(Block validateBlock) {
		ValidateBlock = validateBlock;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	
}

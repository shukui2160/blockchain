package com.tlabs.blockchain.model;

public class ValidateBlock {
	//��֤�ڵ�
	private Block ValidateBlock;
	//��֤״̬
	private String status;
	//��֤��ʼʱ��
	private String StartTime;
	//��֤����ʱ��
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

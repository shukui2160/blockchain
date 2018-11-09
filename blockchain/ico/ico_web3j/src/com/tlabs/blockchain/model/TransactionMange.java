package com.tlabs.blockchain.model;

import java.util.List;

public class TransactionMange {
	private String TXID;
	//���״���ʱ��
	private String TXDateTime;
	private String TXProgress;
	//�������,�����ڱ��ر�
	List<Output> TXOutputs;
	//��������,�����ڱ��ر�
	List<Input> TXInputs;
	List<ValidateBlock> blocks;
	public String getTXID() {
		return TXID;
	}
	public void setTXID(String tXID) {
		TXID = tXID;
	}
	public String getTXDateTime() {
		return TXDateTime;
	}
	public void setTXDateTime(String tXDateTime) {
		TXDateTime = tXDateTime;
	}
	public String getTXProgress() {
		return TXProgress;
	}
	public void setTXProgress(String tXProgress) {
		TXProgress = tXProgress;
	}
	public List<Output> getTXOutputs() {
		return TXOutputs;
	}
	public void setTXOutputs(List<Output> tXOutputs) {
		TXOutputs = tXOutputs;
	}
	public List<Input> getTXInputs() {
		return TXInputs;
	}
	public void setTXInputs(List<Input> tXInputs) {
		TXInputs = tXInputs;
	}
	public List<ValidateBlock> getBlocks() {
		return blocks;
	}
	public void setBlocks(List<ValidateBlock> blocks) {
		this.blocks = blocks;
	}
	
}

package com.minute.service.wallet.entry;

import java.io.Serializable;
import java.util.Date;

public class UserRecord implements Serializable {

	private Long id;
	private String dealId;
	private String txHash;
	private Long userId;
	private Long userCoinId;
	private String coinTokenCode;
	private String oneLeaveType;
	private String twoLeaveType;
	private String typeNote;
	private String value;
	private String status;
	private String recordInfo;
	private Date createTime;
	private Date updateTime;
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserCoinId() {
		return userCoinId;
	}

	public void setUserCoinId(Long userCoinId) {
		this.userCoinId = userCoinId;
	}

	public String getCoinTokenCode() {
		return coinTokenCode;
	}

	public void setCoinTokenCode(String coinTokenCode) {
		this.coinTokenCode = coinTokenCode;
	}

	public String getOneLeaveType() {
		return oneLeaveType;
	}

	public void setOneLeaveType(String oneLeaveType) {
		this.oneLeaveType = oneLeaveType;
	}

	public String getTwoLeaveType() {
		return twoLeaveType;
	}

	public void setTwoLeaveType(String twoLeaveType) {
		this.twoLeaveType = twoLeaveType;
	}

	public String getTypeNote() {
		return typeNote;
	}

	public void setTypeNote(String typeNote) {
		this.typeNote = typeNote;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecordInfo() {
		return recordInfo;
	}

	public void setRecordInfo(String recordInfo) {
		this.recordInfo = recordInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
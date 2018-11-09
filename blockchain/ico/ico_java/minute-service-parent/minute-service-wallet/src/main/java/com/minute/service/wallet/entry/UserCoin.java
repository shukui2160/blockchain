package com.minute.service.wallet.entry;

import java.io.Serializable;
import java.util.Date;

public class UserCoin implements Serializable {

	private Long id;
	private Long walletId;
	private Long coinTokenId;
	private String coinTokenCode;
	private String type;
	private String accountAdd;
	private String accountNum;
	private String accountAvailableNum;
	private String accountLockNum;
	private Date createTime;
	private Date updateTime;
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public Long getCoinTokenId() {
		return coinTokenId;
	}

	public void setCoinTokenId(Long coinTokenId) {
		this.coinTokenId = coinTokenId;
	}

	public String getCoinTokenCode() {
		return coinTokenCode;
	}

	public void setCoinTokenCode(String coinTokenCode) {
		this.coinTokenCode = coinTokenCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountAdd() {
		return accountAdd;
	}

	public void setAccountAdd(String accountAdd) {
		this.accountAdd = accountAdd;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getAccountAvailableNum() {
		return accountAvailableNum;
	}

	public void setAccountAvailableNum(String accountAvailableNum) {
		this.accountAvailableNum = accountAvailableNum;
	}

	public String getAccountLockNum() {
		return accountLockNum;
	}

	public void setAccountLockNum(String accountLockNum) {
		this.accountLockNum = accountLockNum;
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
package com.minute.service.block.entry;

import java.io.Serializable;
import java.util.Date;

public class EthAccount implements Serializable {

	private Long id;
	private String accountAddress;
	private String accountPasswd;
	private String type;
	private Date createTime;
	private Date updateTime;
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountAddress() {
		return accountAddress;
	}

	public void setAccountAddress(String accountAddress) {
		this.accountAddress = accountAddress;
	}

	public String getAccountPasswd() {
		return accountPasswd;
	}

	public void setAccountPasswd(String accountPasswd) {
		this.accountPasswd = accountPasswd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
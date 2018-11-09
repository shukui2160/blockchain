package com.minute.service.user.entry;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
	private Long id;

	private String email;

	private String phone;

	private String passwd;

	private String payPasswd;

	private String status;

	private String markedWord;

	private Date createTime;

	private Date updateTime;

	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getPayPasswd() {
		return payPasswd;
	}

	public void setPayPasswd(String payPasswd) {
		this.payPasswd = payPasswd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMarkedWord() {
		return markedWord;
	}

	public void setMarkedWord(String markedWord) {
		this.markedWord = markedWord;
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
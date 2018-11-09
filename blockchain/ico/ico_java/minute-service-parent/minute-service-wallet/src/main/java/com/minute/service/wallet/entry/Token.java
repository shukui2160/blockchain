package com.minute.service.wallet.entry;

import java.io.Serializable;
import java.util.Date;

public class Token implements Serializable {

	private Long id;
	private Long userId;
	private String tokenCode;
	private String tokenAdd;
	private String tokenLogo;
	private String tokenDes;
	private String circulation;
	private String decimals;
	private Date createTime;
	private Date updateTime;
	private static final long serialVersionUID = 1L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTokenCode() {
		return tokenCode;
	}

	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}

	public String getTokenAdd() {
		return tokenAdd;
	}

	public void setTokenAdd(String tokenAdd) {
		this.tokenAdd = tokenAdd;
	}

	public String getTokenLogo() {
		return tokenLogo;
	}

	public void setTokenLogo(String tokenLogo) {
		this.tokenLogo = tokenLogo;
	}

	public String getTokenDes() {
		return tokenDes;
	}

	public void setTokenDes(String tokenDes) {
		this.tokenDes = tokenDes;
	}

	public String getCirculation() {
		return circulation;
	}

	public void setCirculation(String circulation) {
		this.circulation = circulation;
	}

	public String getDecimals() {
		return decimals;
	}

	public void setDecimals(String decimals) {
		this.decimals = decimals;
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
package com.minute.application.app.entry;

import java.io.Serializable;
import java.util.Date;

public class JoinRecord implements Serializable {

	private Long id;
	private String dealId;
	private Long userId;
	private Long projectId;
	private String tokenNum;
	private String coinProportion;
	private String payNum;
	private String status;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getTokenNum() {
		return tokenNum;
	}

	public void setTokenNum(String tokenNum) {
		this.tokenNum = tokenNum;
	}

	public String getCoinProportion() {
		return coinProportion;
	}

	public void setCoinProportion(String coinProportion) {
		this.coinProportion = coinProportion;
	}

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
package com.minute.application.app.entry;

import java.io.Serializable;
import java.util.Date;

public class Project implements Serializable {
    private Long id;

    private Long userId;

    private String projectLogo;

    private Long teamId;

    private Long tokenId;

    private String tokenNum;

    private String collectNum;

    private String collectedNum;

    private String minInvest;

    private String maxInvest;

    private Date startTime;

    private Date endTime;

    private String status;

    private Long coinId;

    private Date upExchangeTime;

    private String coinProportion;

    private String coinPrice;

    private String walletId;

    private String projectName;

    private String whitePaper;

    private String website;

    private String telegramGroup;

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

    public String getProjectLogo() {
        return projectLogo;
    }

    public void setProjectLogo(String projectLogo) {
        this.projectLogo = projectLogo;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenNum() {
        return tokenNum;
    }

    public void setTokenNum(String tokenNum) {
        this.tokenNum = tokenNum;
    }

    public String getCollectNum() {
        return collectNum;
    }

    public void setCollectNum(String collectNum) {
        this.collectNum = collectNum;
    }

    public String getCollectedNum() {
        return collectedNum;
    }

    public void setCollectedNum(String collectedNum) {
        this.collectedNum = collectedNum;
    }

    public String getMinInvest() {
        return minInvest;
    }

    public void setMinInvest(String minInvest) {
        this.minInvest = minInvest;
    }

    public String getMaxInvest() {
        return maxInvest;
    }

    public void setMaxInvest(String maxInvest) {
        this.maxInvest = maxInvest;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCoinId() {
        return coinId;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    public Date getUpExchangeTime() {
        return upExchangeTime;
    }

    public void setUpExchangeTime(Date upExchangeTime) {
        this.upExchangeTime = upExchangeTime;
    }

    public String getCoinProportion() {
        return coinProportion;
    }

    public void setCoinProportion(String coinProportion) {
        this.coinProportion = coinProportion;
    }

    public String getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(String coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getWhitePaper() {
        return whitePaper;
    }

    public void setWhitePaper(String whitePaper) {
        this.whitePaper = whitePaper;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTelegramGroup() {
        return telegramGroup;
    }

    public void setTelegramGroup(String telegramGroup) {
        this.telegramGroup = telegramGroup;
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
package com.minute.service.wallet.entry;

import java.io.Serializable;
import java.util.Date;

public class SysCoin implements Serializable {
    private Long id;

    private Long coinId;

    private Long walletId;

    private String type;

    private String address;

    private String accountNum;

    private String accountAvailableNum;

    private String accountLockNum;

    private String chainBlance;

    private Long transHashNum;

    private String note;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCoinId() {
        return coinId;
    }

    public void setCoinId(Long coinId) {
        this.coinId = coinId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getChainBlance() {
        return chainBlance;
    }

    public void setChainBlance(String chainBlance) {
        this.chainBlance = chainBlance;
    }

    public Long getTransHashNum() {
        return transHashNum;
    }

    public void setTransHashNum(Long transHashNum) {
        this.transHashNum = transHashNum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
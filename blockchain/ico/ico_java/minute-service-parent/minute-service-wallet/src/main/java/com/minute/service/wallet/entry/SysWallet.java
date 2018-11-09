package com.minute.service.wallet.entry;

import java.io.Serializable;
import java.util.Date;

public class SysWallet implements Serializable {
    private Long id;

    private Long sysUserId;

    private String walletName;

    private String type;

    private Long walletMoney;

    private Long createSysUserId;

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

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getWalletMoney() {
        return walletMoney;
    }

    public void setWalletMoney(Long walletMoney) {
        this.walletMoney = walletMoney;
    }

    public Long getCreateSysUserId() {
        return createSysUserId;
    }

    public void setCreateSysUserId(Long createSysUserId) {
        this.createSysUserId = createSysUserId;
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
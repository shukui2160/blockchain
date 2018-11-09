package com.minute.service.wallet.entry;

import java.io.Serializable;
import java.util.Date;

public class Coin implements Serializable {
    private Long id;

    private String coinName;

    private String coinCode;

    private String coinTodollar;

    private String rechargeMin;

    private String defalutGas;

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

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinCode() {
        return coinCode;
    }

    public void setCoinCode(String coinCode) {
        this.coinCode = coinCode;
    }

    public String getCoinTodollar() {
        return coinTodollar;
    }

    public void setCoinTodollar(String coinTodollar) {
        this.coinTodollar = coinTodollar;
    }

    public String getRechargeMin() {
        return rechargeMin;
    }

    public void setRechargeMin(String rechargeMin) {
        this.rechargeMin = rechargeMin;
    }

    public String getDefalutGas() {
        return defalutGas;
    }

    public void setDefalutGas(String defalutGas) {
        this.defalutGas = defalutGas;
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
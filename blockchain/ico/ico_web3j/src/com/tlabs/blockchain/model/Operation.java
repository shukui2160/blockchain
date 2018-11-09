package com.tlabs.blockchain.model;

import java.util.Date;

/**
 * Created by admin on 2018/7/25.
 */
public class Operation {
    private String operType;
    private String jine;
    private Date operDateTime;
    public String getOperType() {
        return operType;
    }
    public void setOperType(String operType) {
        this.operType = operType;
    }

    public String getJine() {
        return jine;
    }
    public void setJine(String jine) {
        this.jine = jine;
    }
    public Date getOperDateTime() {
        return operDateTime;
    }
    public void setOperDateTime(Date operDateTime) {
        this.operDateTime = operDateTime;
    }
    @Override
    public String toString() {
        return "Operation [operType=" + operType + ", jine=" + jine + ", operDateTime=" + operDateTime + "]";
    }
    public void setJine(float f) {
        // TODO Auto-generated method stub

    }
}

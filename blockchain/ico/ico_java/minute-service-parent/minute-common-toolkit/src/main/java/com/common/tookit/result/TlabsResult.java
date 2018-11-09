package com.common.tookit.result;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TlabsResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;

	private String msg;

	private Object date;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getDate() {
		return date;
	}

	public void setDate(Object date) {
		this.date = date;
	}
    
	@JsonIgnore
	public boolean isSuccess() {
		if ("200".equals(code)) {
			return true;
		} else {
			return false;
		}
	}

}

package com.minute.application.app.config;

import java.util.Date;

public class TlabsToken {
	// 用户Id
	private String userId;
	// 登录后由后台创建
	private Date date;

	// 标示设备类型:android,ios
	private String platform;
	// App版本号:20,21,23....
	private String version;
	// 语言id: 1:中文,2:英文
	private String languageId;
	// 设备Id
	private String deviceId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLanguageId() {
		return languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}

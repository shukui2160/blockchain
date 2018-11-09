package com.common.tookit.result;

public class ResultUtils {

	public static TlabsResult createSuccess() {
		return createSuccess(null);
	}
	
	public static TlabsResult createSuccess(Object data) {
		TlabsResult result = new TlabsResult();
		result.setCode("200");
		result.setMsg("success!");
		result.setDate(data);
		return result;
	}
	
	public static TlabsResult verifyFail() {
		TlabsResult result = new TlabsResult();
		result.setCode("300");
		result.setMsg("parameter exception!");
		return result;
	}
	
	
	
	public static TlabsResult createException() {
		TlabsResult result = new TlabsResult();
		result.setCode("400");
		result.setMsg("system exception!");
		return result;
	}

	// 自定义code值,大于200,小于400,自定义code
	public static TlabsResult createResult(String code, String msg, Object data) {
		TlabsResult result = new TlabsResult();
		result.setCode(code);
		result.setDate(data);
		result.setMsg(msg);
		return result;
	}

	// 自定义code值
	public static TlabsResult createResult(String code, String msg) {
		TlabsResult result = createResult(code, msg, null);
		return result;
	}

}

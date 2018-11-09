package com.common.tookit.result;

public class CommonUtils {

	public static TlabsResult verifyNull(String... strArr) {
		for (String str : strArr) {
			if (str == null || str.length() <= 0) {
				return ResultUtils.verifyFail();
			}
		}
		return null;
	}

}

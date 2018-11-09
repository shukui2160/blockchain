package com.common.tookit.account;

import com.common.tookit.crypto.MD5Utils;

//密码生成规则,加上特定的前缀,防止撞库
public class PasswdUtils {
	public static String buildPasswd(String passwd) {
		return MD5Utils.digestHex("A2nNez43xUSdkESh" + passwd);
	}

}

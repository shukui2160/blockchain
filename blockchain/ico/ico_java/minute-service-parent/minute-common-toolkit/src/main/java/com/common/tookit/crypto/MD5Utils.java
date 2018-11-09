package com.common.tookit.crypto;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class MD5Utils {

	public static String digestHex(String content) {
		Digester md5 = new Digester(DigestAlgorithm.MD5);
		return md5.digestHex(content);
	}

}

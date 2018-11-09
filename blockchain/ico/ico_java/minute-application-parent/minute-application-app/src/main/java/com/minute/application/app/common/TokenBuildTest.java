package com.minute.application.app.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class TokenBuildTest {
	public static void main(String[] args) {
		Map<String, Object> tlabsEnvironmentMap = new HashMap<>();
		tlabsEnvironmentMap.put("platform", "android");
		tlabsEnvironmentMap.put("version", "40");
		tlabsEnvironmentMap.put("languageId", "1");
		String tlabsEnvironment = JSONObject.toJSONString(tlabsEnvironmentMap);
		System.out.println(tlabsEnvironment);
		System.out.println("tlabsEnvironment="+Base64.encode(tlabsEnvironment));
		
		Map<String, Object> tlabsTokenMap = new HashMap<>();
		tlabsTokenMap.put("userId", "24");
		tlabsTokenMap.put("createTime",new Date());
		String tlabsToken = JSONObject.toJSONString(tlabsTokenMap);
		
		String secret ="WP2USLIxxo7W1ePb";
		//构建
	    SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, secret.getBytes());
				
		System.out.println(tlabsToken);
		System.out.println("tlabsToken="+aes.encryptHex(tlabsToken));
		
		
	}

}

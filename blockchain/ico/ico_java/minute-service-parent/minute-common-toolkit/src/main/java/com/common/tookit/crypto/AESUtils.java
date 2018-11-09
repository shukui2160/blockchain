package com.common.tookit.crypto;


import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

public class AESUtils {
	
	//AES解密 16位密钥
	public static String decryptStr(String text ,String secret) {
		SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, secret.getBytes());
		return aes.decryptStr(text);
	}
	
	//AES加密 16位密钥
	public static String encryptHex(String text ,String secret) {
		SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, secret.getBytes());
		return aes.encryptHex(text);
	}
}

package com.minute.service.wallet.service;

import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.entry.SysRecord;

public interface SysWalletService {
	// 系统钱包信息
	TlabsResult SysWalletMange();

	// 查询账户货币地址信息
	TlabsResult SysCoinMange();

	// 查询提币信息
	TlabsResult selectEthColdAddress(String coinName);

	// 提币到冷钱包
	TlabsResult mentionMoneyToColdWallet(String from, String to, String value, SysRecord sysRecord);

	// 向系统热钱包充币
	TlabsResult mentionMoneyToHotWallet(String from, String to, String value, SysRecord sysRecord);

	// 返回狀態值是1的txid
	TlabsResult selectTxidByStatus();

	// 根據txid修改狀態值
	TlabsResult updateStatusByTxid(String txid);
	
	//查询用户钱包信息
	TlabsResult userWalletMange();
	
	//查询用户钱包地址信息
	TlabsResult userWalletAddrMange(String walletId);
	
}

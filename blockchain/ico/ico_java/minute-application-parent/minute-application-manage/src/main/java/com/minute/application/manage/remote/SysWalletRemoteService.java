package com.minute.application.manage.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/wallet/v1")
@FeignClient(name = "minute-service-wallet")
public interface SysWalletRemoteService {

	// 系统钱包信息
	@PostMapping("/sysWallet/sysWalletMange")
	public TlabsResult WalletSysMange();

	// 系统钱包地址信息
	@PostMapping("/sysWallet/sysCoinMange")
	public TlabsResult sysCoinMange();

	// 查询冷钱包的以太坊地址
	@PostMapping("/sysWallet/selectEthColdAddress")
	public TlabsResult selectEthColdAddress();

	// 提币到冷钱包
	@PostMapping("/sysWallet/mentionMoneyToColdWallet")
	public TlabsResult mentionMoneyToColdWallet(@RequestParam(value = "sysName") String sysName,
			@RequestParam(value = "sysWalletId") String sysWalletId, @RequestParam(value = "coinId") String coinId,
			@RequestParam(value = "from") String from, @RequestParam(value = "to") String to,
			@RequestParam(value = "value") String value);

	// 向热钱包充值
	@PostMapping("/sysWallet/mentionMoneyToHotWallet")
	public TlabsResult mentionMoneyToHotWallet(@RequestParam(value = "sysName") String sysName,
			@RequestParam(value = "sysWalletId") String sysWalletId, @RequestParam(value = "coinId") String coinId,
			@RequestParam(value = "from") String from, @RequestParam(value = "to") String to,
			@RequestParam(value = "value") String value);

	// 提币审批同意
	@PostMapping("/coinRecord/agreeDrawCoinToken")
	TlabsResult agreeDrawCoinToken(@RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "toAddress") String toAddress);

	// 提币审批不同意
	@PostMapping("/coinRecord/refuseDrawCoinToken")
	TlabsResult refuseDrawCoinToken(@RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "toAddress") String toAddress);

	// 提币账单详情
	@PostMapping("/wallet/selectBillsByUserCoinId")
	TlabsResult selectBillsByUserCoinId(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "userCoinId") String userCoinId,@RequestParam(value = "type") String type);
	
	//查询userCoinId
	@PostMapping("/wallet/selectCoin")
	TlabsResult selectCoin(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "coinTokenId") Long coinTokenId, @RequestParam(value = "type") String type);
	
	//查询用户钱包信息
	@PostMapping("/sysWallet/userWalletMange")
	public TlabsResult userWalletMange();
	
	//查询用户钱包地址信息
	@PostMapping("/sysWallet/userWalletAddrMange")
	public TlabsResult userWalletAddrMange(@RequestParam(value="walletId")String walletId); 

}

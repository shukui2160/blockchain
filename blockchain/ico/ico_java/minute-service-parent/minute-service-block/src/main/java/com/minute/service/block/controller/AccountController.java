package com.minute.service.block.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.common.tookit.result.TlabsResult;
import com.minute.service.block.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	// 创建普通用户的账户地址
	@PostMapping("/createAccount")
	public TlabsResult createAccount() {
		return accountService.createAccount();
	}

	// 创建项目账户的地址
	@PostMapping("/createProjectAccount")
	public TlabsResult createProjectAccount() {
		return accountService.createProjectAccount();
	}

	// 查询 普通账户的充值记录 (暂时不用)
	@PostMapping("/getTransactionList")
	public TlabsResult getTransactionList(@RequestParam(value = "address") String address,
			@RequestParam(value = "fromBlockNum") String fromBlockNum) {
		return accountService.getTransactionList(address, fromBlockNum);
	}

	// 查询 合约账户的充值记录 （暂不使用）
	@PostMapping("/getContractTransactionList")
	public TlabsResult getContractTransactionList(@RequestParam(value = "address") String address,
			@RequestParam(value = "fromBlockNum") String fromBlockNum) {
		return accountService.getContractTransactionList(address, fromBlockNum);
	}

	// 以太币 转账接口
	// value的单位为wei
	@PostMapping("/ethTransfer")
	public TlabsResult ethTransfer(@RequestParam(value = "fromAdd") String fromAdd,
			@RequestParam(value = "toAdd") String toAdd, @RequestParam(value = "value") String value,
			@RequestParam(value = "gas") String gas) {
		return accountService.ethTransfer(fromAdd, toAdd, value, gas);
	}

	// 代币 转账接口
	// value的单位 为最小,需要根据token的精确度来确定
	@PostMapping("/tokenTransfer")
	public TlabsResult tokenTransfer(@RequestParam(value = "fromAdd") String fromAdd,
			@RequestParam(value = "contractAdd") String contractAdd, @RequestParam(value = "toAdd") String toAdd,
			@RequestParam(value = "value") String value, @RequestParam(value = "gas") String gas) {
		return accountService.tokenTransfer(fromAdd, contractAdd, toAdd, value, gas);
	}

	// ETH余额 和token 余额查询
	@PostMapping("/getBalance")
	public TlabsResult getBalance(@RequestParam(value = "address") String address,
			@RequestParam(value = "contractAdd") String contractAdd) {
		return accountService.getBalance(address, contractAdd);
	}

	// Token详情查询
	@PostMapping("/getTokenInfo")
	TlabsResult getTokenInfo(@RequestParam(value = "contractAdd") String contractAdd) {
		return accountService.getTokenInfo(contractAdd);
	}

	// 根据txHash验证交易是否被打包
	@PostMapping("/validateTransaction")
	TlabsResult validateTransaction(@RequestParam(value = "txHash") String txHash) {
		return accountService.validateTransaction(txHash);
	}

}

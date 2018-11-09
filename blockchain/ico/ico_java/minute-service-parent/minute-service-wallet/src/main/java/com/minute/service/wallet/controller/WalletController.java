package com.minute.service.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.service.WalletService;

//用户相关,注册,登录,忘记密码,更改密码
@RestController
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
	private WalletService walletServicel;

	// 用户 初始化钱包
	@PostMapping("/initWallet")
	public TlabsResult initWallet(@RequestParam(value = "userId") String userId) {
		return walletServicel.initWallet(userId);
	}

	// 查询 账户详情
	@PostMapping("/selectCoin")
	TlabsResult selectCoin(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "coinTokenId") Long coinTokenId, @RequestParam(value = "type") String type) {
		return walletServicel.selectCoin(userId, coinTokenId, type);
	}

	// 参与项目
	@PostMapping("/pay4project")
	TlabsResult pay4project(@RequestParam(value = "userId") String userId, @RequestParam(value = "coinId") Long coinId,
			@RequestParam(value = "dealId") String dealId, @RequestParam(value = "payNum") String payNum,
			@RequestParam(value = "tokenNum") String tokenNum, @RequestParam(value = "languageId") String languageId,
			@RequestParam(value = "projectAdd") String projectAdd, @RequestParam(value = "tokenId") Long tokenId) {
		walletServicel.initTokenAccount(userId, coinId, tokenId);
		return walletServicel.pay4project(userId, coinId, dealId, payNum, tokenNum, languageId, projectAdd, tokenId);
	}

	// 查询用户账户列表
	@PostMapping("/selectCoinsByUserId")
	TlabsResult selectCoinsByUserId(@RequestParam(value = "userId") String userId) {
		return walletServicel.selectCoinsByUserId(userId);
	}

	// 根据userCoinId 查询 账户详情
	@PostMapping("/selectUserCoinById")
	TlabsResult selectUserCoinById(@RequestParam(value = "userId") String userCoinId) {
		return walletServicel.selectUserCoinById(userCoinId);
	}
	
}

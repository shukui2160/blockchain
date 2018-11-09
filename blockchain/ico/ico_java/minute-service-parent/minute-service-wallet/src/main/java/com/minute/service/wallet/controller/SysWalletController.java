package com.minute.service.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.entry.SysRecord;
import com.minute.service.wallet.service.SysWalletService;

@RestController
@RequestMapping("/sysWallet")
public class SysWalletController {

	@Autowired
	private SysWalletService sysWalletService;

	@PostMapping("/sysWalletMange")
	public TlabsResult sysWalletMange() {
		return this.sysWalletService.SysWalletMange();
	}

	@PostMapping("/sysCoinMange")
	public TlabsResult sysCoinMange() {
		return this.sysWalletService.SysCoinMange();
	}

	@PostMapping("/selectEthColdAddress")
	public TlabsResult selectEthColdAddress(@RequestParam(value="coinName",defaultValue="eth")String coinName) {
		return this.sysWalletService.selectEthColdAddress(coinName);
	}

	@PostMapping("/mentionMoneyToColdWallet")
	public TlabsResult mentionMoneyToColdWallet(String from, String to, String value, SysRecord sysRecord) {
		return this.sysWalletService.mentionMoneyToColdWallet(from, to, value, sysRecord);
	}

	@PostMapping("/mentionMoneyToHotWallet")
	public TlabsResult mentionMoneyToHotWallet(String from, String to, String value, SysRecord sysRecord) {
		return this.sysWalletService.mentionMoneyToHotWallet(from, to, value, sysRecord);
	}

	@PostMapping("/selectTxidByStatus")
	public TlabsResult selectTxidByStatus() {
		return this.sysWalletService.selectTxidByStatus();
	}

	@PostMapping("/updateStatusByTxid")
	public TlabsResult updateStatusByTxid(@RequestParam(value="txid")String txid) {
		return this.sysWalletService.updateStatusByTxid(txid);
	}
	
	@PostMapping("/userWalletMange")
	public TlabsResult userWalletMange() {
		return this.sysWalletService.userWalletMange();
	}
	
	@PostMapping("/userWalletAddrMange")
	public TlabsResult userWalletAddrMange(@RequestParam(value="walletId")String walletId) {
		return this.sysWalletService.userWalletAddrMange(walletId);
	}
	
	
}

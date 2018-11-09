package com.minute.application.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.TlabsResult;
import com.minute.application.manage.remote.SysWalletRemoteService;


@RestController
@RequestMapping("/sysWallet")
public class SysWalletController {

	@Autowired
	private SysWalletRemoteService sysWalletRemoteService;
	

	@PostMapping("/sysWalletMange")
	public TlabsResult SysWalletMange() {
		return this.sysWalletRemoteService.WalletSysMange();
	}

	@PostMapping("/sysCoinMange")
	public TlabsResult sysCoinMange() {
		return this.sysWalletRemoteService.sysCoinMange();
	}
	
	@PostMapping("/selectEthColdAddress")
	public TlabsResult selectEthColdAddress() {
		return this.sysWalletRemoteService.selectEthColdAddress();
	}
	
	@PostMapping("/mentionMoneyToColdWallet")
	public TlabsResult mentionMoneyToColdWallet(@RequestBody JSONObject request) {
		String sys_name = request.getString("sys_name")==null?"":request.getString("sys_name");
		String id = request.getString("id")==null?"":request.getString("id");
		String coin_id = request.getString("coin_id")==null?"":request.getString("coin_id");
		String from = request.getString("from")==null?"":request.getString("from");
		String to = request.getString("to")==null?"":request.getString("to");
		String value = request.getString("value")==null?"":request.getString("value");
		return this.sysWalletRemoteService.mentionMoneyToColdWallet(sys_name, id, coin_id, from, to,value);
	}
	
	@PostMapping("/mentionMoneyToHotWallet")
	public TlabsResult mentionMoneyToHotWallet(@RequestBody JSONObject request) {
		String sys_name = request.getString("sys_name")==null?"":request.getString("sys_name");
		String id = request.getString("id")==null?"":request.getString("id");
		String coin_id = request.getString("coin_id")==null?"":request.getString("coin_id");
		String from = request.getString("from")==null?"":request.getString("from");
		String to = request.getString("to")==null?"":request.getString("to");
		String value = request.getString("value")==null?"":request.getString("value");
		return this.sysWalletRemoteService.mentionMoneyToHotWallet(sys_name, id, coin_id,from, to,value);
	}
}

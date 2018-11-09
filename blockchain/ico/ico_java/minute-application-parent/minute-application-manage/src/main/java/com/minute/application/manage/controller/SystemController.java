package com.minute.application.manage.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.TlabsResult;
import com.minute.application.manage.remote.SysWalletRemoteService;
import com.minute.application.manage.remote.SystemRemoteService;
import com.minute.application.manage.service.MentionMoneyService;
@RestController
@RequestMapping("/system")
public class SystemController {

	@Autowired
	private SystemRemoteService systemService;
	
	@Autowired
	private MentionMoneyService mentionMoneyService;
	
	@Autowired
	private SysWalletRemoteService sysWalletRemoteService;
	
	
	@PostMapping("/systemUserLogin")
	public TlabsResult systemUserLogin(@RequestBody JSONObject request) {
		String name = request.getString("name") == null?"":request.getString("name");
		String passwd = request.getString("passwd") == null?"":request.getString("passwd");
		return this.systemService.sysUserLogin(name, passwd);
	}
	@PostMapping("/mentionMoneyMange")
	public TlabsResult mentionMontyMange() {
		return this.mentionMoneyService.mentionMoneyMange();
	}
	
	@PostMapping("/mentionThrough")
	public TlabsResult mentionThrough(@RequestBody JSONObject request) {
		String id = request.getString("id") == null?"":request.getString("id");
		String status = request.getString("status") == null?"":request.getString("status");
		return this.mentionMoneyService.mentionThrough(Long.valueOf(id),status);
	}
	
	@PostMapping("/mentionReject")
	public TlabsResult mentionReject(@RequestBody JSONObject request) {
		String id = request.getString("id") == null?"":request.getString("id");
		String status = request.getString("status") == null?"":request.getString("status");
		return this.mentionMoneyService.mentionReject(Long.valueOf(id), status);
	}
	
	@PostMapping("/selectBillsByUserCoinId")
	public TlabsResult selectBillsByUserCoinId(@RequestBody JSONObject request) {
		String userId = request.getString("userId")==null?"":request.getString("userId");
		String userCoinId = request.getString("userCoinId")==null?"":request.getString("userCoinId");
		String type = request.getString("type")==null?"":request.getString("type");
		return this.sysWalletRemoteService.selectBillsByUserCoinId(userId, userCoinId,type);
	}
	
	@PostMapping("/userWalletMange")
	public TlabsResult userWalletMange() {
		return this.sysWalletRemoteService.userWalletMange();
	}
	
	@PostMapping("/userWalletAddrMange")
	public TlabsResult userWalletAddrMange(@RequestBody JSONObject request) {
		String walletId = request.getString("walletId")==null?"":request.getString("walletId");
		return this.sysWalletRemoteService.userWalletAddrMange(walletId);
	}
}

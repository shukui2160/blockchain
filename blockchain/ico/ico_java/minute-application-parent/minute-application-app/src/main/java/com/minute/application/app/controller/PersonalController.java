package com.minute.application.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.CommonUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.app.config.RequestContext;
import com.minute.application.app.config.RequireLogin;
import com.minute.application.app.service.PersonalService;

//个人中心相关
@RestController
@RequestMapping("/personal")
public class PersonalController {

	@Autowired
	private PersonalService personalService;

	// 个人中心 首页信息
	@RequireLogin
	@PostMapping("/index")
	public TlabsResult index() {
		String userId = RequestContext.get().getUserId();
		return personalService.index(userId);
	}

	// 个人中心 资产列表
	@RequireLogin
	@PostMapping("/listCoin")
	public TlabsResult listCoin() {
		String userId = RequestContext.get().getUserId();
		return personalService.listCoin(userId);
	}

	// 用户 账单列表 ()
	@RequireLogin
	@PostMapping("/listBills")
	public TlabsResult listBills(@RequestBody JSONObject request) {
		String userCoinId = request.getString("userCoinId");
		String index = request.getString("index");
		TlabsResult verifyResult = CommonUtils.verifyNull(userCoinId, index);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return personalService.listBills(Long.valueOf(userId), Long.valueOf(userCoinId), index);
	}

	// 账单详情
	@RequireLogin
	@PostMapping("/bileInfo")
	public TlabsResult bileInfo(@RequestBody JSONObject request) {
		// 账单表 主键
		String userRecordId = request.getString("userRecordId");
		TlabsResult verifyResult = CommonUtils.verifyNull(userRecordId);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return personalService.bileInfo(userId, userRecordId);
	}

	// 获取ETH手续费
	@RequireLogin
	@PostMapping("/ethGasValue")
	public TlabsResult ethGasValue(@RequestBody JSONObject request) {
		String coinId = request.getString("coinId");
		TlabsResult verifyResult = CommonUtils.verifyNull(coinId);
		if (verifyResult != null) {
			return verifyResult;
		}
		return personalService.ethGasValue(coinId);
	}

	// 提币 (法币 或者 token)
	@RequireLogin
	@PostMapping("/drawCoinToken")
	public TlabsResult drawCoinToken(@RequestBody JSONObject request) {
		String payPassWd = request.getString("payPassWd");
		String userCoinId = request.getString("userCoinId");
		String toAddress = request.getString("toAddress");
		String value = request.getString("value");
		String coinTokenId = request.getString("coinTokenId");
		String type = request.getString("type");
		TlabsResult verifyResult = CommonUtils.verifyNull(payPassWd, userCoinId, toAddress, value);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		String languageId = RequestContext.get().getLanguageId();
		return personalService.drawCoinToken(payPassWd, userCoinId, userId, coinTokenId, type, toAddress, value,
				languageId);
	}

}

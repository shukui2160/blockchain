package com.minute.service.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.service.CoinRecordService;

//账单生成
@RestController
@RequestMapping("/coinRecord")
public class CoinRecordController {

	@Autowired
	private CoinRecordService coinRecordService;

	// 根据用户id 和 userCoinId 查询账单
	@PostMapping("/listBills")
	TlabsResult listBills(@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userCoinId") Long userCoinId, @RequestParam(value = "index") String index) {
		return coinRecordService.listBills(userId, userCoinId, index);
	}

	// 根据用户id 和 账单id查看账单 详情
	@PostMapping("/bileInfo")
	TlabsResult bileInfo(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "userRecordId") String userRecordId) {
		return coinRecordService.bileInfo(userId, userRecordId);
	}

	// 根据coin表主键 查询coin数据
	@PostMapping("/getCoinInfo")
	TlabsResult getCoinInfo(@RequestParam(value = "coinId") String coinId) {
		return coinRecordService.getCoinInfo(coinId);
	}

	// 生成提币申请 记录 状态为进行中
	@PostMapping("/drawCoinToken")
	TlabsResult drawCoinToken(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "userCoinId") String userCoinId, @RequestParam(value = "toAddress") String toAddress,
			@RequestParam(value = "value") String value, @RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "languageId") String languageId) {
		return coinRecordService.drawCoinToken(userId, dealId, userCoinId, toAddress, value, languageId);
	}

	// 运营审批通过的时候 调用
	// 生成提币申请 记录 状态为进行中
	@PostMapping("/agreeDrawCoinToken")
	TlabsResult agreeDrawCoinToken(@RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "toAddress") String toAddress) {
		return coinRecordService.agreeDrawCoinToken(dealId, toAddress);
	}

	// 运营审批失败的时候 调用
	// 生成提币申请 记录 状态为进行中
	@PostMapping("/refuseDrawCoinToken")
	TlabsResult refuseDrawCoinToken(@RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "toAddress") String toAddress) {
		return coinRecordService.refuseDrawCoinToken(dealId, toAddress);
	}

	// 法币账户充值记录
	// 链上 服务 回调
	@PostMapping("/coinRecharge")
	TlabsResult coinRecharge(@RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "txHash") String txHash, @RequestParam(value = "toAddress") String toAddress,
			@RequestParam(value = "value") String value) {
		return coinRecordService.coinRecharge(dealId,txHash,toAddress,value);
	}

}

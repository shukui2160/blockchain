package com.minute.application.app.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.app.common.Dic4Wallet;
import com.minute.application.app.entry.MentionRecord;
import com.minute.application.app.mapper.MentionRecordMapper;
import com.minute.application.app.remote.UserRemoteService;
import com.minute.application.app.remote.WalletRemoteService;
import com.minute.application.app.service.PersonalService;

import cn.hutool.core.util.RandomUtil;

@Service
@Transactional
public class PersonalServiceImpl implements PersonalService {

	@Autowired
	private WalletRemoteService walletRemoteService;

	@Autowired
	private UserRemoteService userRemoteService;

	@Autowired
	private MentionRecordMapper mentionRecordMapper;

	@Override
	public TlabsResult index(String userId) {
		TlabsResult walletResult = walletRemoteService.selectCoin(userId, 2L, Dic4Wallet.WalletCoinType.COIN.getKey());
		if (!walletResult.isSuccess()) {
			return walletResult;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> walletData = (Map<String, String>) walletResult.getDate();
		Map<String, String> data = new HashMap<>();
		data.put("userId", userId);
		data.put("accountNum", walletData.get("accountNum"));
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult listCoin(String userId) {
		TlabsResult walletResult = walletRemoteService.selectCoinsByUserId(userId);
		if (!walletResult.isSuccess()) {
			return walletResult;
		}
		return ResultUtils.createSuccess(walletResult.getDate());
	}

	@Override
	public TlabsResult listBills(Long userId, Long userCoinId, String index) {
		TlabsResult walletResult = walletRemoteService.listBills(userId, userCoinId, index);
		if (!walletResult.isSuccess()) {
			return walletResult;
		}
		return walletResult;
	}

	@Override
	public TlabsResult bileInfo(String userId, String userRecordId) {
		// 账单详情
		TlabsResult walletResult = walletRemoteService.bileInfo(userId, userRecordId);
		if (!walletResult.isSuccess()) {
			return walletResult;
		}

		// 调用其他服务 查询账单详情
		return walletResult;
	}

	@Override
	public TlabsResult ethGasValue(String coinId) {
		TlabsResult walletResult = walletRemoteService.getCoinInfo(coinId);
		if (!walletResult.isSuccess()) {
			return walletResult;
		}
		JSONObject walletData = JSONObject.parseObject(JSONObject.toJSONString(walletResult.getDate()));
		Map<String, Object> data = new HashMap<>();
		data.put("defalutGas", walletData.getString("defalutGas"));
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult drawCoinToken(String payPassWd, String userCoinId, String userId, String coinTokenId,
			String type, String toAddress, String value, String languageId) {
		// 1: 验证支付密码 是否正确
		TlabsResult userResult = userRemoteService.authPayPasswd(userId, payPassWd, languageId);
		if (!userResult.isSuccess()) {
			return userResult;
		}
		TlabsResult walletResult = walletRemoteService.selectCoin(userId, Long.valueOf(coinTokenId), type);
		if (!walletResult.isSuccess()) {
			return walletResult;
		}
		JSONObject walletData = JSONObject.parseObject(JSONObject.toJSONString(walletResult.getDate()));
		String fromAdd = walletData.getString("accountAdd");
		// 生成 提现记录 -状态为待审批
		MentionRecord record = new MentionRecord();
		record.setDealId(RandomUtil.randomUUID());
		record.setUserId(Long.valueOf(userId));
		record.setCoinTokenId(Long.valueOf(coinTokenId));
		record.setType(type);
		record.setFormAddr(fromAdd);
		record.setToAddr(toAddress);
		record.setMoneyValue(value);
		mentionRecordMapper.insertSelective(record);
		// 用钱包系统-生成提现记录 状态为进行中
		TlabsResult result = walletRemoteService.drawCoinToken(userId, userCoinId, toAddress, value, record.getDealId(), languageId);
		if (!result.isSuccess()) {
			// 手动回滚事物
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return result;
		}
		return ResultUtils.createSuccess();
	}

}

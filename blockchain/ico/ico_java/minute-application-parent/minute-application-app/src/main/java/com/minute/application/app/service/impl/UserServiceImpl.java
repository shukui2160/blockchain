package com.minute.application.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.common.tookit.crypto.AESUtils;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.app.remote.UserRemoteService;
import com.minute.application.app.remote.WalletRemoteService;
import com.minute.application.app.service.UserService;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

@Service
public class UserServiceImpl implements UserService {

	@Value("${com.tlabs.application.app.secret}")
	private String secret;

	@Autowired
	private UserRemoteService userRemoteService;

	@Autowired
	private WalletRemoteService walletRemoteService;

	@Override
	public TlabsResult expireCheck(String tlabsToken) {
		String token = AESUtils.decryptStr(tlabsToken, secret);
		Date date = JSONObject.parseObject(token).getDate("createTime");
		long minute = DateUtil.between(date, new Date(), DateUnit.MINUTE);
		if (minute > 60 * 24 * 30) {
			return ResultUtils.createSuccess(true);
		} else {
			return ResultUtils.createSuccess(false);
		}
	}

	@Override
	public TlabsResult regist(String email, String code, String passwd, String markedWord, String languageId) {
		TlabsResult result = userRemoteService.regist(email, code, passwd, markedWord, languageId);
		// 初始化钱包 改为登录的时候初始化
		// 业务层面解决分布式事物问题
		return result;
	}

	@Override
	public TlabsResult login(String email, String passwd, String languageId) {
		TlabsResult result = userRemoteService.login(email, passwd, languageId);
		if (!"200".equals(result.getCode())) {
			return result;
		}
		JSONObject userData = JSONObject.parseObject(JSONObject.toJSONString(result.getDate()));
		String userId = userData.getString("userId");
		// 初始化钱包成功 则返回成功
		TlabsResult walletResult = walletRemoteService.initWallet(userId);
		if ("200".equals(walletResult.getCode())) {
			JSONObject token = new JSONObject();
			token.put("userId", userId);
			token.put("createTime", System.currentTimeMillis());
			Map<String, String> data = new HashMap<>();
			data.put("tlabsToken", AESUtils.encryptHex(token.toJSONString(), this.secret));
			walletResult.setDate(data);
			return walletResult;
		} else {
			return walletResult;
		}
	}

}

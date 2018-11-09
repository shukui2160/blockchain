package com.minute.service.wallet.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.tookit.coin.DecimalUtils;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.entry.Token;
import com.minute.service.wallet.mapper.TokenMapper;
import com.minute.service.wallet.service.TokenService;

@Service
@Transactional
public class TokenServiceImpl implements TokenService {

	@Autowired
	private TokenMapper tokenMapper;

	@Override
	public TlabsResult createToken(String userId, String tokenAdd, String tokenLogo, String circulation,
			String tokenCode, String tokenDes, String decimals) {
		Token token = new Token();
		token.setUserId(Long.valueOf(userId));
		// 验证token是否有效，无效直接return
		token.setTokenCode(tokenCode);
		token.setTokenAdd(tokenAdd);
		token.setTokenLogo(tokenLogo);
		token.setCirculation(circulation);
		token.setDecimals(DecimalUtils.toDecimals(decimals));
		token.setTokenDes(tokenDes);
		tokenMapper.insertSelective(token);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult listTokens(Long userId) {
		List<Map<String, String>> data = tokenMapper.selectByUserId(Long.valueOf(userId));
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult selectToken(Long tokenId) {
		Token token = tokenMapper.selectByPrimaryKey(tokenId);
		Map<String, Object> data = new HashMap<>();
		data.put("tokenCode", token.getTokenCode());
		data.put("tokenAdd", token.getTokenAdd());
		data.put("tokenLogo", token.getTokenLogo());
		data.put("tokenDes", token.getTokenDes());
		data.put("circulation", token.getCirculation());
		data.put("decimals", token.getDecimals());
		return ResultUtils.createSuccess(data);
	}

}

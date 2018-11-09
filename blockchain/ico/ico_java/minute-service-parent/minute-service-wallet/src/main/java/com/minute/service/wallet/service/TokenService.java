package com.minute.service.wallet.service;

import com.common.tookit.result.TlabsResult;

public interface TokenService {

	TlabsResult createToken(String userId, String tokenAdd, String tokenLogo, String circulation, String tokenCode,
			String tokenDes, String decimals);

	TlabsResult listTokens(Long userId);

	TlabsResult selectToken(Long tokenId);

}

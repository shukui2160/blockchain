package com.minute.service.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.service.TokenService;

//token 相关操作
@RestController
@RequestMapping("/token")
public class TokenController {

	@Autowired
	private TokenService tokenService;

	// 创建token
	@PostMapping("/createToken")
	TlabsResult createToken(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "tokenAdd") String tokenAdd, @RequestParam(value = "tokenLogo") String tokenLogo,
			@RequestParam(value = "circulation") String circulation,
			@RequestParam(value = "tokenCode") String tokenCode, @RequestParam(value = "tokenDes") String tokenDes,
			@RequestParam(value = "decimals") String decimals) {
		return tokenService.createToken(userId, tokenAdd, tokenLogo, circulation, tokenCode, tokenDes,decimals);
	}

	// 根据用户id 查询token列表
	@PostMapping("/listTokens")
	TlabsResult listTokens(@RequestParam(value = "userId") Long userId) {
		return tokenService.listTokens(userId);
	}

	// 根据tokenId查询token
	@PostMapping("/selectToken")
	TlabsResult selectToken(@RequestParam(value = "tokenId") Long tokenId) {
		return tokenService.selectToken(tokenId);

	}

}

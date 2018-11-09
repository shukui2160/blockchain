package com.minute.application.app.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/wallet/v1")
@FeignClient(name = "minute-service-wallet")
public interface WalletRemoteService {

	@PostMapping("/wallet/initWallet")
	TlabsResult initWallet(@RequestParam(value = "userId") String userId);

	// 查询coin详情
	@PostMapping("/wallet/selectCoin")
	TlabsResult selectCoin(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "coinTokenId") Long coinTokenId, @RequestParam(value = "type") String type);

	@PostMapping("/wallet/pay4project")
	TlabsResult pay4project(@RequestParam(value = "userId") String userId, @RequestParam(value = "coinId") Long coinId,
			@RequestParam(value = "dealId") String dealId, @RequestParam(value = "payNum") String payNum,
			@RequestParam(value = "tokenNum") String tokenNum, @RequestParam(value = "languageId") String languageId,
			@RequestParam(value = "projectAdd") String projectAdd, @RequestParam(value = "tokenId") Long tokenId);

	// 查询用户账户列表
	@PostMapping("/wallet/selectCoinsByUserId")
	TlabsResult selectCoinsByUserId(@RequestParam(value = "userId") String userId);

	// 根据userCoinId 查询 账户详情
	@PostMapping("/wallet/selectUserCoinById")
	TlabsResult selectUserCoinById(@RequestParam(value = "userId") String userCoinId);

	// -------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------

	// 查询用户账单 列表
	@PostMapping("/coinRecord/listBills")
	TlabsResult listBills(@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "userCoinId") Long userCoinId, @RequestParam(value = "index") String index);

	@PostMapping("/coinRecord/bileInfo")
	TlabsResult bileInfo(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "userRecordId") String userRecordId);

	@PostMapping("/coinRecord/drawCoinToken")
	TlabsResult drawCoinToken(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "userCoinId") String userCoinId, @RequestParam(value = "toAddress") String toAddress,
			@RequestParam(value = "value") String value, @RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "languageId") String languageId);

	// 根据coin表主键 查询coin数据
	@PostMapping("/coinRecord/getCoinInfo")
	TlabsResult getCoinInfo(@RequestParam(value = "coinId") String coinId);

	// -------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------

	@PostMapping("/token/createToken")
	TlabsResult createToken(@RequestParam(value = "userId") String userId,
			@RequestParam(value = "tokenAdd") String tokenAdd, @RequestParam(value = "tokenLogo") String tokenLogo,
			@RequestParam(value = "circulation") String circulation,
			@RequestParam(value = "tokenCode") String tokenCode, @RequestParam(value = "tokenDes") String tokenDes,
			@RequestParam(value = "decimals") String decimals);

	@PostMapping("/token/listTokens")
	TlabsResult listTokens(@RequestParam(value = "userId") Long userId);

	@PostMapping("/token/selectToken")
	TlabsResult selectToken(@RequestParam(value = "tokenId") Long tokenId);
	
	// -------------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------------
	@PostMapping("/coin/defalutGas")
	TlabsResult defalutGas(@RequestParam(value = "coinName") String coinName);
}

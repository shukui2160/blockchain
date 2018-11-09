package com.minute.service.wallet.service;

import org.apache.ibatis.annotations.Param;

import com.common.tookit.result.TlabsResult;

public interface WalletService {

	TlabsResult initWallet(String userId);

	TlabsResult selectCoin(String userId, Long coinTokenId, String type);
	
	void initTokenAccount(String userId, Long coinId, Long tokenId);

	TlabsResult pay4project(String userId, Long coinId, String dealId, String payNum, String tokenNum,
			String languageId, String projectAdd, Long tokenId);

	TlabsResult selectCoinsByUserId(String userId);

	TlabsResult selectUserCoinById(String userCoinId);
	
	TlabsResult selectBillsByUserCoinId(Long userId, Long userCoinId,String type); 

}

package com.minute.service.wallet.service;


import com.common.tookit.result.TlabsResult;

public interface CoinService {
	//查询法币的基本信息
	TlabsResult mangeOfCoin();
	
	TlabsResult selectCoinName(Long id);
	
	TlabsResult selectMangeBycoinName(String coinName);
	
}

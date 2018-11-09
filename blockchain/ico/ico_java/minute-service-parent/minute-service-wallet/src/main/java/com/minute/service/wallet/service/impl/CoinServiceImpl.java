package com.minute.service.wallet.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.entry.Coin;
import com.minute.service.wallet.mapper.CoinMapper;
import com.minute.service.wallet.service.CoinService;

@Service
@Transactional
public class CoinServiceImpl implements CoinService{

	@Autowired
	private CoinMapper coinMapper;

	
	
	@Override
	public TlabsResult mangeOfCoin() {
		// TODO Auto-generated method stub
		List<Coin> mangeOfCoin = this.coinMapper.mangeOfCoin();
		List<Map<String, Object>> data = new ArrayList<>();
		for (Coin coinCoin : mangeOfCoin) {
			String coin_name = coinCoin.getCoinName();
			String coin_code = coinCoin.getCoinCode();
			Map<String, Object> coinMange = new HashMap<>();
			coinMange.put("coin_name", coin_name);
			coinMange.put("coin_code", coin_code);
			coinMange.put("defalut_gas", coinCoin.getDefalutGas());
			coinMange.put("say_renewal", coinCoin.getDefalutGas());
			data.add(coinMange);
		}
		return ResultUtils.createSuccess(data);
	}



	@Override
	public TlabsResult selectCoinName(Long id) {
		// TODO Auto-generated method stub
		return ResultUtils.createSuccess();
	}



	@Override
	public TlabsResult selectMangeBycoinName(String coinName) {
		// TODO Auto-generated method stub
		String defalutGas =  this.coinMapper.selectMangeBycoinName(coinName);
		Map<String, String> data = new HashMap<String,String>();
		data.put("defalutGas",defalutGas);
		return ResultUtils.createSuccess(data);
	}

}

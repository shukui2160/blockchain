package com.minute.service.wallet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.service.CoinService;

@RestController
@RequestMapping("/coin")
public class CoinController {

	@Autowired
	private CoinService coinService;

	@PostMapping("/coinMange")
	public TlabsResult coinMange() {
		return this.coinService.mangeOfCoin();
	}
	
	@PostMapping("/defalutGas")
	public TlabsResult defalutGas(@RequestParam(value = "coinName") String coinName) {
		return this.coinService.selectMangeBycoinName(coinName);
	}
}

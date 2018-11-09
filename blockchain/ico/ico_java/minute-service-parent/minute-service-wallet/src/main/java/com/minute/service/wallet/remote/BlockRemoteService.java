package com.minute.service.wallet.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/block/v1")
@FeignClient(name = "minute-service-block")
public interface BlockRemoteService {
	// 创建地址
	@PostMapping("/account/createAccount")
	public TlabsResult batchMakeAccount();

	// 转账
	@PostMapping("/account/ethTransfer")
	public TlabsResult ethTransfer(@RequestParam(value = "fromAdd") String fromAdd,
			@RequestParam(value = "toAdd") String toAdd, @RequestParam(value = "value") String value,
			@RequestParam(value = "gas") String gas);

	// 代币 转账接口
	// value的单位 为最小,需要根据token的精确度来确定
	@PostMapping("/account/tokenTransfer")
	public TlabsResult tokenTransfer(@RequestParam(value = "fromAdd") String fromAdd,
			@RequestParam(value = "contractAdd") String contractAdd, @RequestParam(value = "toAdd") String toAdd,
			@RequestParam(value = "value") String value, @RequestParam(value = "gas") String gas);

}

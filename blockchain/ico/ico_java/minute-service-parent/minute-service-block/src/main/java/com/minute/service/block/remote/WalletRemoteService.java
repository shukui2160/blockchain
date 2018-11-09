package com.minute.service.block.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/wallet/v1")
@FeignClient(name = "minute-service-wallet")
public interface WalletRemoteService {

	@PostMapping("/coinRecord/coinRecharge")
	TlabsResult coinRecharge(@RequestParam(value = "dealId") String dealId,
			@RequestParam(value = "txHash") String txHash, @RequestParam(value = "toAddress") String toAddress,
			@RequestParam(value = "value") String value);

}

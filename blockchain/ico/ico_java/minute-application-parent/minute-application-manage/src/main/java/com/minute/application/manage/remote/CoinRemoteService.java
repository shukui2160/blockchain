package com.minute.application.manage.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/wallet/v1")
@FeignClient(name = "minute-service-wallet")
public interface CoinRemoteService {

	@PostMapping("/coin/coinMange")
	public TlabsResult coinMange();
}

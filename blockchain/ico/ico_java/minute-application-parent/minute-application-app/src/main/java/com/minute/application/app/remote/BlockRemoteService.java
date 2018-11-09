package com.minute.application.app.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/block/v1")
@FeignClient(name = "minute-service-block")
public interface BlockRemoteService {

	@PostMapping("/account/createAccount")
	TlabsResult createAccount();

	@PostMapping("/account/getBalance")
	TlabsResult getBalance(@RequestParam(value = "address") String address,
			@RequestParam(value = "contractAdd") String contractAdd);

	@PostMapping("/account/createProjectAccount")
	TlabsResult createProjectAccount();

	@PostMapping("/account/getTokenInfo")
	TlabsResult getTokenInfo(@RequestParam(value = "contractAdd")String contractAdd);
}

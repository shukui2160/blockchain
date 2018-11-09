package com.minute.service.wallet.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/user/v1")
@FeignClient(name = "minute-service-user")
public interface UserRemoteService {
	
	@PostMapping("/user/selectEamilById")
	public TlabsResult selectEmailById(@RequestParam(value = "userId") String userId); 
	
}

package com.minute.service.wallet.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/system/v1")
@FeignClient(name = "minute-service-system")
public interface SystemRemoteService {
	// 根据主键查询用户name
	@PostMapping("/system/selectNameByid")
	public TlabsResult selectNameByid(@RequestParam(value = "id") String id);
}

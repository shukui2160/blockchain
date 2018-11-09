package com.minute.application.manage.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/cms/v1")
@FeignClient(name = "minute-service-cms")
public interface LanContextRemoteService {

	@PostMapping("/project/contextPange")
	TlabsResult selectContext(@RequestParam(value = "topicIds") String topicIds);
}

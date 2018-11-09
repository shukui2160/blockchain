package com.minute.application.app.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.common.tookit.result.TlabsResult;

@RequestMapping("/cms/v1")
@FeignClient(name = "minute-service-cms")
public interface CmsRemoteService {

	@PostMapping("/project/insertContext")
	TlabsResult insertContext(@RequestParam(value = "lanId") Long lanId, @RequestParam(value = "topicId") Long topicId,
			@RequestParam(value = "topicType") String topicType, @RequestParam(value = "context") String context);

	@PostMapping("/project/selectContext")
	TlabsResult selectContext(@RequestParam(value = "topicIds") String topicIds,
			@RequestParam(value = "topicType") String topicType, @RequestParam(value = "lanId") String lanId);

	@PostMapping("/project/updateContext")
	TlabsResult updateContext(@RequestParam(value = "lanId") Long lanId, @RequestParam(value = "topicId") Long topicId,
			@RequestParam(value = "topicType") String topicType, @RequestParam(value = "context") String context);
}

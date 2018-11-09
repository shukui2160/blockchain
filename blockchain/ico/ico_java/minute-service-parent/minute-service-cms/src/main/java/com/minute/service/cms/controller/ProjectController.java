package com.minute.service.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.tookit.result.TlabsResult;
import com.minute.service.cms.service.ProjectService;

@RestController
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@PostMapping("/insertContext")
	TlabsResult insertContext(@RequestParam(value = "lanId") Long lanId, @RequestParam(value = "topicId") Long topicId,
			@RequestParam(value = "topicType") String topicType, @RequestParam(value = "context") String context) {
		return projectService.insertContext(lanId, topicId, topicType, context);
	}

	@PostMapping("/selectContext")
	TlabsResult selectContext(@RequestParam(value = "topicIds") String topicIds,
			@RequestParam(value = "topicType") String topicType, @RequestParam(value = "lanId") String lanId) {
		return projectService.selectContext(topicIds, topicType, lanId);
	}
	
	@PostMapping("/contextPange")
	TlabsResult contextPange(@RequestParam(value = "topicIds") String topicIds) {
		return projectService.contextMange(topicIds);
	}
	
	@PostMapping("/updateContext")
	TlabsResult updateContext(@RequestParam(value = "lanId") Long lanId, @RequestParam(value = "topicId") Long topicId,
			@RequestParam(value = "topicType") String topicType, @RequestParam(value = "context") String context) {
		return projectService.updateContext(lanId, topicId, topicType, context);
	}

}

package com.minute.application.app.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.TlabsResult;
import com.minute.application.app.config.RequestContext;
import com.minute.application.app.config.RequireLogin;
import com.minute.application.app.service.ProjectService;

@RestController
@RequestMapping("/personalProject")
public class PersonalProjectController {

	@Autowired
	private ProjectService projectService;

	@RequireLogin
	@PostMapping("/createProjectByUserId")
	public TlabsResult createProjectByUserId() throws Exception {
		String userId = RequestContext.get().getUserId();
		return this.projectService.createProjectByUserId(userId);
	}

	@RequireLogin
	@PostMapping("/joinProjectByUserId")
	public TlabsResult joinProjectByUserId() {
		String userId = RequestContext.get().getUserId();
		return this.projectService.joinProjectByUserId(userId);
	}

	@RequireLogin
	@PostMapping("/selectProjectById")
	public TlabsResult selectProjectById(@RequestBody JSONObject request) throws Exception {
		String id = request.getString("projectId") == null ? "" : request.getString("projectId");
		return this.projectService.selectProjectMangeById(id);
	}

	@RequireLogin
	@PostMapping("/makeSureProject")
	public TlabsResult makeSureProject(@RequestBody JSONObject request) {
		String id = request.getString("projectId") == null ? "" : request.getString("projectId");
		return this.projectService.makeSureProject(id);
	}

	@RequireLogin
	@PostMapping("/makeSure")
	public TlabsResult makeSure(@RequestBody JSONObject request) {
		String id = request.getString("projectId") == null ? "" : request.getString("projectId");
		Date startTime = request.getDate("startTime");
		Date endTime = request.getDate("endTime");
		return this.projectService.makeSure(id,startTime,endTime);
	}
}

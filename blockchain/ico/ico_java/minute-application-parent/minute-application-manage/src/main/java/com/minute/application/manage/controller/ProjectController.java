package com.minute.application.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.manage.entry.ApproveRecord;
import com.minute.application.manage.remote.LanContextRemoteService;
import com.minute.application.manage.service.ProjectService;


@RestController
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private LanContextRemoteService lanContext;
	
	//项目列表
	@PostMapping("/queryByStatus")
	public TlabsResult queryByStatus(@RequestBody JSONObject request) {
		String status = request.getString("status") == null ?"":request.getString("status");
		String project_name = request.getString("project_name") == null?"":request.getString("project_name");
		return ResultUtils.createSuccess(this.projectService.queryByStatus(status,project_name));
	}
	
	//项目审批记录列表
	@PostMapping("/projectApproveRecord")
	public TlabsResult projectApproveRecord(String status) {
		return ResultUtils.createSuccess(this.projectService.projectApproveRecord(status));
	}
	
	//项目详情列表
	@PostMapping("/projectDetails")
	public TlabsResult projectDetails(@RequestBody JSONObject request) {
		String topic_id = request.getString("topic_id") == null?"":request.getString("topic_id");
		return this.lanContext.selectContext(topic_id);
	}
	
	//项目通过
	@PostMapping("/projectThrough")
	public TlabsResult projectThrough(@RequestBody JSONObject request) {
		String status = request.getString("status") == null?"":request.getString("status");
		String projectName = request.getString("projectName") == null?"":request.getString("projectName");
		String id = request.getString("id") == null?"":request.getString("id");
		String sysUserId= request.getString("sysUserId") == null?"":request.getString("sysUserId");
		ApproveRecord approveRecord = new ApproveRecord();
		approveRecord.setStatus(status);
		approveRecord.setSysUserId(Long.valueOf(sysUserId));
		approveRecord.setProjectName(projectName);
		this.projectService.projectThrough(status,Long.valueOf(id),approveRecord);
		return ResultUtils.createSuccess();
	};
	
	//项目驳回
	@PostMapping("/projectreject")
	public TlabsResult projectReject(@RequestBody JSONObject request) {
		String status = request.getString("status") == null?"":request.getString("status");
		String projectName = request.getString("projectName") == null?"":request.getString("projectName");
		String id = request.getString("id") == null?"":request.getString("id");
		String sysUserId= request.getString("sysUserId") == null?"":request.getString("sysUserId");
		ApproveRecord approveRecord = new ApproveRecord();
		approveRecord.setStatus(status);
		approveRecord.setSysUserId(Long.valueOf(sysUserId));
		approveRecord.setProjectName(projectName);
		this.projectService.projectreject(status,Long.valueOf(id),approveRecord);
		return ResultUtils.createSuccess();
	};

}

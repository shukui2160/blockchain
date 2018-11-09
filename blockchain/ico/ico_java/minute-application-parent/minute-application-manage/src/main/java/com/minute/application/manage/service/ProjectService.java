package com.minute.application.manage.service;

import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;
import com.minute.application.manage.entry.ApproveRecord;

public interface ProjectService {
	// 项目列表
	public TlabsResult queryByStatus(@RequestParam(value = "status") String status,
			@RequestParam(value = "project_name") String project_name);

	// 项目审批记录列表
	public TlabsResult projectApproveRecord(String status);

	// 项目通过
	public TlabsResult projectThrough(String status, Long id, ApproveRecord approveRecord);

	// 项目驳回
	public TlabsResult projectreject(String status, Long id, ApproveRecord approveRecord);

}

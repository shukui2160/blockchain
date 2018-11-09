package com.minute.application.manage.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.manage.entry.ApproveRecord;
import com.minute.application.manage.entry.Project;
import com.minute.application.manage.mapper.ApproveRecordMapper;
import com.minute.application.manage.mapper.ProjectMapper;
import com.minute.application.manage.service.ProjectService;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private ApproveRecordMapper approveRecordMapper;

	// 项目列表
	@Override
	public TlabsResult queryByStatus(String status, String project_name) {
		// TODO Auto-generated method stub
		List<Project> projectList = this.projectMapper.selectProjectByStatus(status, project_name);
		return ResultUtils.createSuccess(projectList);
	}

	//项目审批记录表
	@Override
	public TlabsResult projectApproveRecord(String status) {
		// TODO Auto-generated method stub
		return ResultUtils.createSuccess(this.approveRecordMapper.projectApproveRecord(status));
	}

	// 项目通过
	@Override
	public TlabsResult projectThrough(String status, Long id, ApproveRecord approveRecord) {
		// TODO Auto-generated method stub
		int m = 0;
		// 修改项目状态值
		m += this.projectMapper.updateProjectStatus(status, id);
		// 添加到审批记录表中
		m += this.approveRecordMapper.insertProjectApproveRecord(approveRecord);
		if (m >= 2) {
			return ResultUtils.createSuccess();
		} else {
			return ResultUtils.createException();
		}
	}

	// 项目驳回
	@Override
	public TlabsResult projectreject(String status, Long id, ApproveRecord approveRecord) {
		// TODO Auto-generated method stub
		int m = 0;
		// 修改项目状态值
		m += this.projectMapper.updateProjectStatus(status, id);
		// 添加到审批记录表中
		m += this.approveRecordMapper.insertProjectApproveRecord(approveRecord);
		if (m > 2) {
			return ResultUtils.createSuccess();
		} else {
			return ResultUtils.createException();
		}
	}

}

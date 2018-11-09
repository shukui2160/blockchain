package com.minute.application.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.minute.application.app.entry.ProjectAccount;

@Mapper
public interface ProjectAccountMapper {
	int deleteByPrimaryKey(Long id);

	int insert(ProjectAccount record);

	int insertSelective(ProjectAccount record);

	ProjectAccount selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ProjectAccount record);

	int updateByPrimaryKey(ProjectAccount record);

	ProjectAccount selectByProjectId(Long projectId);
}
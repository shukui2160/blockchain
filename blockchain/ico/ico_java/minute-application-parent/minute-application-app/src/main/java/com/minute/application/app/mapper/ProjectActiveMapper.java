package com.minute.application.app.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.minute.application.app.entry.ProjectActive;

@Mapper
public interface ProjectActiveMapper {
	int deleteByPrimaryKey(Long id);

	int insert(ProjectActive record);

	int insertSelective(ProjectActive record);

	ProjectActive selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(ProjectActive record);

	int updateByPrimaryKey(ProjectActive record);

	ProjectActive selectByProjectId(Long projectId);

	ProjectActive selectByProjectIdForUpdate(Long projectId);

}
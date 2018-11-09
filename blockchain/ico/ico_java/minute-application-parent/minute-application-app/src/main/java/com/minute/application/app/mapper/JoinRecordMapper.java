package com.minute.application.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.application.app.entry.JoinRecord;

@Mapper
public interface JoinRecordMapper {

	int deleteByPrimaryKey(Long id);

	int insert(JoinRecord record);

	int insertSelective(JoinRecord record);

	JoinRecord selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(JoinRecord record);

	int updateByPrimaryKey(JoinRecord record);

 	//查询我参加的项目
	List<JoinRecord> joinProjectByUserId(@Param("userId") Integer userId);
	
	//项目结束后修改项目状态值
	int updateStatusByProjectId(@Param("projectId") Long projectId);

	

}
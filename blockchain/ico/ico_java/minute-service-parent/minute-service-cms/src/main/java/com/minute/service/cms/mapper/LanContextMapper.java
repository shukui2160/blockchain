package com.minute.service.cms.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.cms.entry.LanContext;

@Mapper
public interface LanContextMapper {
	
	int deleteByPrimaryKey(Long id);

	int insert(LanContext record);

	int insertSelective(LanContext record);

	LanContext selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(LanContext record);

	int updateByPrimaryKeyWithBLOBs(LanContext record);

	int updateByPrimaryKey(LanContext record);

	String selectContext(@Param("topicId") Long topicId, @Param("topicType") String topicType,
			@Param("lanId") Long lanId);
	
	String contextMange(@Param("topicId") Long topicId);
	
	
	LanContext selectByTopicIdAndTopicType(@Param("topicId") Long topicId, @Param("topicType") String topicType,
			@Param("lanId") Long lanId);
}
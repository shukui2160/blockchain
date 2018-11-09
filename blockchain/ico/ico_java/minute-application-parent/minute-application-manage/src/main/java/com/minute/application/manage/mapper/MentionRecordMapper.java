package com.minute.application.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.application.manage.entry.MentionRecord;

@Mapper
public interface MentionRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MentionRecord record);

    int insertSelective(MentionRecord record);

    MentionRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MentionRecord record);

    int updateByPrimaryKey(MentionRecord record);
    
 // select mentionMoney mange
 	List<MentionRecord> mentionMoneyMange();

 	// update status by id
 	int updateStatusById(@Param("status")String status,@Param("id")Long id);
}
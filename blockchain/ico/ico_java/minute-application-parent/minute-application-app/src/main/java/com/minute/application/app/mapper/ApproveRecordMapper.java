package com.minute.application.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.minute.application.app.entry.ApproveRecord;

@Mapper
public interface ApproveRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ApproveRecord record);

    int insertSelective(ApproveRecord record);

    ApproveRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApproveRecord record);

    int updateByPrimaryKey(ApproveRecord record);
}
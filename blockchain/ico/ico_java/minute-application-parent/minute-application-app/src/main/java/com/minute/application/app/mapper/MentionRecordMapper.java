package com.minute.application.app.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.minute.application.app.entry.MentionRecord;

@Mapper
public interface MentionRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MentionRecord record);

    int insertSelective(MentionRecord record);

    MentionRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MentionRecord record);

    int updateByPrimaryKey(MentionRecord record);
}
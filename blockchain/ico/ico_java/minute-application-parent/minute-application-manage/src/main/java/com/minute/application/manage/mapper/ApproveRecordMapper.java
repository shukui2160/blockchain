package com.minute.application.manage.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.application.manage.entry.ApproveRecord;
@Mapper
public interface ApproveRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ApproveRecord record);

    int insertSelective(ApproveRecord record);

    ApproveRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApproveRecord record);

    int updateByPrimaryKey(ApproveRecord record);
    
    //查询审核列表,根据状态排序
    List<ApproveRecord> projectApproveRecord(@Param("status")String status);
    
    //添加记录
    int  insertProjectApproveRecord(ApproveRecord ApproveRecord);
}
package com.minute.service.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.wallet.entry.SysRecord;
@Mapper
public interface SysRecordMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysRecord record);

    int insertSelective(SysRecord record);

    SysRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRecord record);

    int updateByPrimaryKey(SysRecord record);
    
    //select Txid By Status
    List<String> selectTxidByStatus();
    
    //update Status By Txid
    int updateStatusByTxid(@Param("txid")String txid);
}
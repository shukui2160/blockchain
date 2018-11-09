package com.minute.service.block.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.minute.service.block.entry.EthIncome;

@Mapper
public interface EthIncomeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EthIncome record);

    int insertSelective(EthIncome record);

    EthIncome selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EthIncome record);

    int updateByPrimaryKey(EthIncome record);

	List<EthIncome> selectByStatus(String status);
}
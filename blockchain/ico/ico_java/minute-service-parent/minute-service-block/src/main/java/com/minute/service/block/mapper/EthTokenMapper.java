package com.minute.service.block.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.minute.service.block.entry.EthToken;

@Mapper
public interface EthTokenMapper {
	int deleteByPrimaryKey(Long id);

	int insert(EthToken record);

	int insertSelective(EthToken record);

	EthToken selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(EthToken record);

	int updateByPrimaryKey(EthToken record);

	EthToken selectByContractAddress(String contractAddress);

}
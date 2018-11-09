package com.minute.service.block.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.minute.service.block.entry.EthAccount;

@Mapper
public interface EthAccountMapper {
	

	int deleteByPrimaryKey(Long id);

	int insert(EthAccount record);

	int insertSelective(EthAccount record);

	EthAccount selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(EthAccount record);

	int updateByPrimaryKey(EthAccount record);

	EthAccount selectByAccountAddress(String accountAddress);
}
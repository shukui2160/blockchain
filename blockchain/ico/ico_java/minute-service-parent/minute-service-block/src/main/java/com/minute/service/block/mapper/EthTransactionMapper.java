package com.minute.service.block.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.block.entry.EthTransaction;

@Mapper
public interface EthTransactionMapper {

	

	int deleteByPrimaryKey(Long id);

	int insert(EthTransaction record);

	int insertSelective(EthTransaction record);

	EthTransaction selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(EthTransaction record);

	int updateByPrimaryKey(EthTransaction record);

	List<EthTransaction> selectRechargeRecords(@Param("toAddress") String toAddress,
			@Param("fromBlockNum") Long fromBlockNum, @Param("toBlockNum") Long toBlockNum);

}
package com.minute.service.block.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.block.entry.EthBlock;

@Mapper
public interface EthBlockMapper {

	int deleteByPrimaryKey(Long id);

	int insert(EthBlock record);

	int insertSelective(EthBlock record);

	EthBlock selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(EthBlock record);

	int updateByPrimaryKey(EthBlock record);

	List<EthBlock> selectByStatus(@Param("blockNum") Long blockNum, @Param("status") String status);

	EthBlock selectByblockNum(Long blockNum);

	EthBlock selectLatestBlock();
}
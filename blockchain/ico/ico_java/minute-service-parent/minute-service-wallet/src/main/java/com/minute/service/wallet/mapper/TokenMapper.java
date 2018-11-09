package com.minute.service.wallet.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.minute.service.wallet.entry.Token;

@Mapper
public interface TokenMapper {

	int deleteByPrimaryKey(Long id);

	int insert(Token record);

	int insertSelective(Token record);

	Token selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(Token record);

	int updateByPrimaryKey(Token record);

	// 根据用户id 查询用户 拥有的token
	List<Map<String, String>> selectByUserId(Long userId);
}
package com.minute.service.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.wallet.entry.UserRecord;

@Mapper
public interface UserRecordMapper {

	int deleteByPrimaryKey(Long id);

	int insert(UserRecord record);

	int insertSelective(UserRecord record);

	UserRecord selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UserRecord record);

	int updateByPrimaryKey(UserRecord record);

	List<UserRecord> selectBillsByUserCoinId(@Param("userId") Long userId, @Param("userCoinId") Long userCoinId);

	UserRecord selectByDealId(String dealId);
	
	List<UserRecord> selectBillsByUserCoinIdType(@Param("userId") Long userId, @Param("userCoinId") Long userCoinId,@Param("type") String type);
}
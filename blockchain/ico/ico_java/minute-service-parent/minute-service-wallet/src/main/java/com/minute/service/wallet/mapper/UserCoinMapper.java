package com.minute.service.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.minute.service.wallet.entry.UserCoin;

@Mapper
public interface UserCoinMapper {

	int deleteByPrimaryKey(Long id);

	int insert(UserCoin record);

	int insertSelective(UserCoin record);

	UserCoin selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UserCoin record);

	int updateByPrimaryKey(UserCoin record);

	// 查询coin详情
	UserCoin selectByWalletId(@Param("walletId") Long walletId, @Param("coinTokenId") Long coinTokenId,
			@Param("type") String type);

	UserCoin selectByWalletIdForUpdate(@Param("walletId") Long walletId, @Param("coinTokenId") Long coinTokenId,
			@Param("type") String type);

	List<UserCoin> selectCoinsByWalletId(@Param("walletId") Long walletId);

	UserCoin selectByAccountAdd(String accountAdd);
}
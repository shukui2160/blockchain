package com.minute.service.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.minute.service.wallet.entry.UserWallet;

@Mapper
public interface UserWalletMapper {
	int deleteByPrimaryKey(Long id);

	int insert(UserWallet record);

	int insertSelective(UserWallet record);

	UserWallet selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(UserWallet record);

	int updateByPrimaryKey(UserWallet record);

	UserWallet selectByUserId(Long userId);
	
	//查询用户钱包信息
	List<UserWallet> userWalletMange();

}
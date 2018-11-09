package com.minute.service.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.wallet.entry.Coin;

@Mapper
public interface CoinMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Coin record);

    int insertSelective(Coin record);

    Coin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Coin record);

    int updateByPrimaryKey(Coin record);
    
    // 查询法币的基本信息
 	List<Coin> mangeOfCoin();
 	
 	//select coin_name by id
 	String selectCoinName(@Param("id")Long id);
 	
 	//select defalut_gas by coin_name
 	String selectMangeBycoinName(@Param("coinName")String coinName);
}
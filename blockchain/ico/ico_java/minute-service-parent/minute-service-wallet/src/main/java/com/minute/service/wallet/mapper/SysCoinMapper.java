package com.minute.service.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.minute.service.wallet.entry.SysCoin;
@Mapper
public interface SysCoinMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysCoin record);

    int insertSelective(SysCoin record);

    SysCoin selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysCoin record);

    int updateByPrimaryKey(SysCoin record);
    
    //查询账户货币地址信息
    List<SysCoin> sysCoinMange();
    
    //查询冷钱包eth的地址
    String selectEthColdAddress();
    
    //查询热钱包eth的地址
    String selectEthHotAddress();
}
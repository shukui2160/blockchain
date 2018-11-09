package com.minute.service.wallet.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.wallet.entry.SysWallet;
@Mapper
public interface SysWalletMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysWallet record);

    int insertSelective(SysWallet record);

    SysWallet selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysWallet record);

    int updateByPrimaryKey(SysWallet record);
    
    //查询系统钱包信息
    List<SysWallet> sysWalletMange();
    
    //查询系统钱包name根据id
    String selectWalletById(@Param("id")Long id);
}
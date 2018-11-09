package com.minute.service.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.minute.service.system.entry.SysUser;

@Mapper
public interface SysUserMapper {
	int deleteByPrimaryKey(Long id);

	int insert(SysUser record);

	int insertSelective(SysUser record);

	SysUser selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(SysUser record);

	int updateByPrimaryKey(SysUser record);

	// 登录
	SysUser sysLogin(@Param(value = "name") String name, @Param("passwd") String passwd);
	
	//查询用户名根据主键
	String selectNameById(@Param(value="id")Long id);
}
package com.minute.service.system.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.system.entry.SysUser;
import com.minute.service.system.mapper.SysUserMapper;
import com.minute.service.system.service.SystemUserService;


@Service
@Transactional
public class SystemUserServiceImpl implements SystemUserService{

	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Override
	public TlabsResult sysLogin(String name, String passwd) {
		// TODO Auto-generated method stub
		SysUser user = this.sysUserMapper.sysLogin(name, passwd);
		System.out.println(user);
		if(user==null) {
			return ResultUtils.createResult("201", "登录失败");
		}else{
			return ResultUtils.createSuccess();
		}
	}

	@Override
	public TlabsResult selectNameById(String id) {
		// TODO Auto-generated method stub
		return ResultUtils.createSuccess(this.sysUserMapper.selectByPrimaryKey(Long.valueOf(id)));
	}

}

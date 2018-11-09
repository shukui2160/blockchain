package com.minute.service.system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.tookit.result.TlabsResult;
import com.minute.service.system.service.SystemUserService;
@RestController
@RequestMapping("/system")
public class SystemContorller {

	@Autowired
	private SystemUserService systemUserService;
	
	@PostMapping("/sysUserLogin")
	public TlabsResult sysUserLogin(@RequestParam(value="name")String name,@RequestParam(value="passwd")String passwd) {
		//this.systemUserService.sysLogin(name, passwd);
		return this.systemUserService.sysLogin(name, passwd);
	}
	
	@PostMapping("/selectNameByid")
	public TlabsResult selectNameByid(@RequestParam(value="id")String id) {
		return this.systemUserService.selectNameById(id);
	}
	
	
}

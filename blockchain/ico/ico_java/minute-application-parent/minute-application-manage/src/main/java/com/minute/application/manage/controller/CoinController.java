
package com.minute.application.manage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.manage.remote.CoinRemoteService;



@RestController
@RequestMapping("/coin")
public class CoinController{
	
	@Autowired
	private CoinRemoteService coinRemoteService;
	
	@PostMapping("/coinMange")
	public TlabsResult coinMange() {	
		return ResultUtils.createResult("200", "select success", this.coinRemoteService.coinMange());
	}
}

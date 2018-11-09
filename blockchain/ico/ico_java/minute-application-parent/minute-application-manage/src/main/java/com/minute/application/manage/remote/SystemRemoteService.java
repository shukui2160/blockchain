package com.minute.application.manage.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/system/v1")
@FeignClient(name = "minute-service-system")
public interface SystemRemoteService {

	// 系统用户登录
	@PostMapping("/system/sysUserLogin")
	public TlabsResult sysUserLogin(@RequestParam(value = "name") String name,
			@RequestParam(value = "passwd") String passwd);

	// 根据主键查询用户name
	@PostMapping("/system/selectNameByid")
	public TlabsResult selectNameByid(@RequestParam(value = "id") String id);

	// 提币信息
	@PostMapping("/mentionMoney/mentionMoneyMange")
	public TlabsResult mentionMontyMange();

	/*// 提币通过
	@PostMapping("/mentionMoney/mentionThrough")
	public TlabsResult mentionThrough(@RequestParam(value = "id") String id,
			@RequestParam(value = "passwd") String passwd, @RequestParam(value = "status") String status,
			@RequestParam(value = "coinName") String coinName, @RequestParam(value = "userName") String userName,
			@RequestParam(value = "fromAddr") String fromAddr, @RequestParam(value = "toAddr") String toAddr,
			@RequestParam(value = "moneyValue") String moneyValue, @RequestParam(value = "gas") String gas);

	// 提币驳回
	@PostMapping("/mentionMoney/mentionReject")
	public TlabsResult mentionReject(@RequestParam(value = "id") String id,
			@RequestParam(value = "status") String status, @RequestParam(value = "coinName") String coinName,
			@RequestParam(value = "userName") String userName, @RequestParam(value = "fromAddr") String fromAddr,
			@RequestParam(value = "toAddr") String toAddr, @RequestParam(value = "moneyValue") String moneyValue,
			@RequestParam(value = "gas") String gas);
*/
}

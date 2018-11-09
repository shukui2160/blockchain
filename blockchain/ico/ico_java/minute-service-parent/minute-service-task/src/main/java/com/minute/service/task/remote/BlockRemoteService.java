package com.minute.service.task.remote;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.tookit.result.TlabsResult;

@RequestMapping("/block/v1")
@FeignClient(name = "minute-service-block")
public interface BlockRemoteService {

	// 获取需要同步的区块列表
	@PostMapping("/block/getBlockList")
	public TlabsResult getBlockList();

	// 同步区块信息
	@PostMapping("/block/synBlock")
	public TlabsResult synBlock(@RequestParam(value = "blockNum") String blockNum);

	// 通知钱包系统生成 充值记录
	@PostMapping("/block/notifyWallet")
	public TlabsResult notifyWallet();
}

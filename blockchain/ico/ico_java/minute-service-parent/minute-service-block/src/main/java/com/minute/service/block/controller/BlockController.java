package com.minute.service.block.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.block.service.BlockService;

@RestController
@RequestMapping("/block")
public class BlockController {

	@Autowired
	private BlockService blockService;

	// 定时任务调用
	// 获取需要同步的区块列表
	@PostMapping("/getBlockList")
	public TlabsResult getBlockList() {
		return blockService.getBlockList();
	}

	// 定时任务调用
	// 同步区块信息
	@PostMapping("/synBlock")
	public TlabsResult synBlock(@RequestParam(value = "blockNum") String blockNum) {
		blockService.synBlock(blockNum);
		return ResultUtils.createSuccess();
	}

	// 定时任务调用
	// 通知钱包系统-生成法币或代币 充值记录
	@PostMapping("/notifyWallet")
	public TlabsResult notifyWallet() {
		return blockService.notifyWallet();
	}

	// 手动调用 初始化block
	// 同步区块信息
	@PostMapping("/initBlock")
	public TlabsResult initBlock(@RequestParam(value = "fromNum") Long fromNum,
			@RequestParam(value = "toNum") Long toNum) {
		blockService.initBlock(fromNum, toNum);
		return ResultUtils.createSuccess();
	}

}

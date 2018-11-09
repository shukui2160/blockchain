package com.minute.service.task.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.TlabsResult;
import com.minute.service.task.remote.BlockRemoteService;

@Component
public class BlockTask {

	@Autowired
	private BlockRemoteService blockRemoteService;

	private static final Logger log = LoggerFactory.getLogger(BlockTask.class);

	@Scheduled(cron = "0 0/2 0/1 * * ?")
	public void reportCurrentTime() {
		TlabsResult result = blockRemoteService.getBlockList();
		log.info("开始同步区块:" + JSONObject.toJSONString(result));
		if (result.isSuccess()) {
			String data = JSON.toJSONString(result.getDate());
			List<JSONObject> array = JSON.parseArray(data, JSONObject.class);
			// array长度最长为20
			for (JSONObject json : array) {
				String blockNum = json.getString("blockNum");
				TlabsResult synResult = blockRemoteService.synBlock(blockNum);
				if (synResult.isSuccess()) {
					log.info("正在同步区块:" + blockNum);
				} else {
					log.info("同步区块失败:" + JSONObject.toJSONString(synResult));
				}
			}
		} else {
			log.info("获取同步区块列表失败:" + JSONObject.toJSONString(result));
		}
	}

	@Scheduled(cron = "0 0/2 0/1 * * ?")
	public void nitifyWallet() {
		TlabsResult result = blockRemoteService.notifyWallet();
		log.info("通知wallet服务生成充值记录:" + JSONObject.toJSONString(result));
	}

}

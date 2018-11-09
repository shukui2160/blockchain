package com.minute.service.task.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
public class TestTask {

	
    
	/**
	 * cron表达式在线生成地址：
	 * http://cron.qqe2.com/
	*/
	//每十分钟执行一次
	@Scheduled(cron = "0 0/10 0/1 * * ?")
	public void reportCurrentTime() {
		System.out.println("hello world!");
	}

}

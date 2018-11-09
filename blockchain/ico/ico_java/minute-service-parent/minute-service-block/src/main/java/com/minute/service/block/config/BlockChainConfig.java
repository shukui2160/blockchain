package com.minute.service.block.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.minute.common.eth.EthTemplate;

@Configuration
public class BlockChainConfig {

	@Value("${geth.rpc}")
	private String rpc;

	@Bean
	public EthTemplate ethTemplate() {
		return new EthTemplate(rpc);
	}

}

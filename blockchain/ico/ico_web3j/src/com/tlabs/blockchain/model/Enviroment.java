package com.tlabs.blockchain.model;

import cn.hutool.setting.dialect.Props;

public class Enviroment {
	public static enum ethEnviromentType{
		ETHTEST_ENVIROMENT("ethTest","http://0.0.0.0:8545"),
		ETHDEV_ENVIROMENT("ethDev",""),
		ETHPRD_ENVIROMENT("ethPrd","");
		private String key;
		private String value;
		private ethEnviromentType(String key, String value) {
			this.key = key;
			this.value = value;
		}
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
	}
}

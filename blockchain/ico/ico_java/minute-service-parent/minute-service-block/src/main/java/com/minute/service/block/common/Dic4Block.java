package com.minute.service.block.common;

public class Dic4Block {

	public enum EthBlockSatatus {
		UN_SYN("0", "未同步"), SYN("1", "已同步");

		private String key;
		private String value;

		private EthBlockSatatus(String type, String msg) {
			this.key = type;
			this.value = msg;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

}

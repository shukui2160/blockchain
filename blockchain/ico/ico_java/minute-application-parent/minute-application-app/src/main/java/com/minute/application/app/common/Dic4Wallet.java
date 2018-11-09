package com.minute.application.app.common;

public class Dic4Wallet {

	

	public enum WalletCoinType {
		COIN("0", "法币"), TOKEN("1", "代币");

		private String key;
		private String value;

		private WalletCoinType(String type, String msg) {
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

package com.minute.service.wallet.common;

public class Dic4Wallet {

	public enum WalletType {
		USER_WALLET("1", "普通钱包,用户注册后分配");

		private String key;
		private String value;

		private WalletType(String type, String msg) {
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

	public enum CoinType {
		BIT("1", "比特币"), ETH("2", "以太币");

		private String key;
		private String value;

		private CoinType(String type, String msg) {
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
	
	public enum UserStatus {
		HAVING_LOCKS("0", "已锁定"), UN_LOCK("1", "未锁定,正常用户！");

		private String key;
		private String value;

		private UserStatus(String type, String msg) {
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

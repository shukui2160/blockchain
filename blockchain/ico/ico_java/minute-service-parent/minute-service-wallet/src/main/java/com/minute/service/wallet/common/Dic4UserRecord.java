package com.minute.service.wallet.common;

public class Dic4UserRecord {

	public enum ONE_LEAVE_TYPE {
		// 充值：之支持法币充值

		// 提现：可以是法币 也可以代币

		// 项目：参与项目()

		IN_PUT("0", "收入"), OUT_PUT("1", "支出");

		private String key;
		private String value;

		private ONE_LEAVE_TYPE(String type, String msg) {
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

	public enum TWO_LEAVE_TYPE {
		// 充值：之支持法币充值

		// 提现：可以是法币 也可以代币

		// 项目：参与项目()

		RECHARGE("0", "法币-充值"), DRAW_COIN("1", "法币-提现"), DRAW_TOKIN("2", "代币-提现"), JOIN_PROJECT("4", "参与项目");

		private String key;
		private String value;

		private TWO_LEAVE_TYPE(String type, String msg) {
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

	public enum STATUSA {

		RUNNING("0", "进行中"), FINISH("1", "已完成"), FAIL("2", "失败");

		private String key;
		private String value;

		private STATUSA(String type, String msg) {
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

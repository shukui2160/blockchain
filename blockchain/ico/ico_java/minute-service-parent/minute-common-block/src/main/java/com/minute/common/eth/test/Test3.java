package com.minute.common.eth.test;

import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.minute.common.eth.EthTemplate;

public class Test3 {
	public static void main(String[] args) {
		// Web3j web3j = Web3j.build(new HttpService("http://106.2.3.17:8545"));
		EthTemplate template = new EthTemplate("http://106.2.3.17:8546");
		//0x2b4f613f23a7be0faf6d78c3d13dc6c72537bf20613a1d15d9970f7f515991ee
		
		//0xad77bd5eb72a43af8dfda92ff8f7a9aa79127beee479adc3197dc2cd7349a24d
		TransactionReceipt record = template.getTransactionReceiptByHash("0x2b4f613f23a7be0faf6d78c3d13dc6c72537bf20613a1d15d9970f7f515991ee");
		System.out.println(record);
	}

}

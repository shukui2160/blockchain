package com.minute.common.eth.test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.http.HttpService;

import rx.functions.Action1;

public class Test1 {
	public static void main(String[] args) {

		Web3j web3j = Web3j.build(new HttpService("http://106.2.3.17:8545"));
		// Web3j web3j = Web3j.build(new HttpService("http://106.2.3.17:8546"));
		web3j.transactionObservable().subscribe(new Action1<Transaction>() {
			@Override
			public void call(Transaction t) {
				System.err.println("txHash:" + t.getHash());
			}
		});
	}

}

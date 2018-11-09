package com.minute.common.eth.test;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;

import rx.functions.Action1;

public class Test2 {
	public static void main(String[] args) {

		Web3j web3j = Web3j.build(new HttpService("http://106.2.3.17:8545"));
		web3j.blockObservable(false).subscribe(new Action1<EthBlock>() {

			@Override
			public void call(EthBlock t) {
				System.out.println(t.getBlock().getNumber());
			}
		});

	}
}

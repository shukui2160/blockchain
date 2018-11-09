package com.tlabs.blockchain.Test;
import java.math.BigDecimal;

import org.web3j.protocol.core.DefaultBlockParameter;

import com.tlabs.blockchain.model.Enviroment;
import com.tlabs.blockchain.realize.ETHWallet;

public class Test {
	public static void main(String[] args) throws Exception {
			ETHWallet eth = new ETHWallet();
			eth.Connection(Enviroment.ethEnviromentType.ETHTEST_ENVIROMENT);
			//eth.GenerateAddress("11188juu");
			//eth.Transfer("0xbb410578e329d73b81c7d01f1aa211cbdd6565c8", "0x328dc699823426b9d0e18bdf018e2d5d223fc2f9", "123", new BigDecimal(123), new BigDecimal(12));
			
			
			eth.leafOfAddress("0xbfb2e296d9cf3e593e79981235aed29ab9984c0f");
	}
}

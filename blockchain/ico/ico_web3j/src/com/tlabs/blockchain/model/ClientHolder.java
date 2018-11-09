package com.tlabs.blockchain.model;

import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;

public class ClientHolder {
	private static String ip="http://localhost:8545/";
	private static Parity parity;

	private void ParityClient() {
	}

	private static class SetClientHolder {
		private static final Parity parity = Parity.build(new HttpService(ip));
	}

	public static final Parity getParity() {
		return ClientHolder.parity;
	}
}

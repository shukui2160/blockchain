package com.tlabs.blockchain.realize;

import java.math.BigDecimal;
import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.http.HttpService;

import com.tlabs.blockchain.iminterface.IWallet;
import com.tlabs.blockchain.model.Address;
import com.tlabs.blockchain.model.Enviroment;
import com.tlabs.blockchain.model.EnviromentType;
import com.tlabs.blockchain.model.TransactionMange;

public class USTDWallet implements IWallet {

	@Override
	public void Connection(Enviroment enviroment) {
		// TODO Auto-generated method stub

	}

	@Override
	public String GenerateAddress(String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void GetWalletInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void BackupWallet() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OutputPrivateKeys() {
		// TODO Auto-generated method stub

	}

	@Override
	public String Transfer(String to, String from, String privateKey, BigDecimal amount, BigDecimal gasPrice) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void GetTXProcess() {
		// TODO Auto-generated method stub

	}

	@Override
	public TransactionMange GetTXInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean leafOfAddress(String address) {
		// TODO Auto-generated method stub
		return false;
	}

}

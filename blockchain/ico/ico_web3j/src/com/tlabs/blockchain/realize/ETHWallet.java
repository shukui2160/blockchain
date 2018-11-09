package com.tlabs.blockchain.realize

;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthProtocolVersion;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.tx.TransactionManager;
import org.web3j.protocol.admin.Admin;

import com.tlabs.blockchain.iminterface.IWallet;
import com.tlabs.blockchain.model.Address;
import com.tlabs.blockchain.model.Enviroment;
import com.tlabs.blockchain.model.Enviroment.ethEnviromentType;
import com.tlabs.blockchain.model.EnviromentType;
import com.tlabs.blockchain.model.TransactionMange;

import cn.hutool.setting.dialect.Props;

public class ETHWallet implements IWallet {
	private static String ip;
	private volatile static Web3j web3j;
	private volatile static Admin admin;
	private volatile static Parity parity;

	// init
	public void init() {
		ip = null;
		web3j = null;
		admin = null;
		parity = null;
	}

	@Override
	public void Connection(Enviroment enviroment) {
		// Enviroment.ethEnviromentType.ETHTEST_ENVIROMENT.getValue();
	}

	// connection
	public void Connection(ethEnviromentType ethEnviroment) {
		if (ethEnviroment.equals(Enviroment.ethEnviromentType.ETHTEST_ENVIROMENT)) {
			ip = Enviroment.ethEnviromentType.ETHTEST_ENVIROMENT.getValue();
			Web3j web3j = Web3j.build(new HttpService(ip));
			System.out.println("the Test of Eth connect success");
		} else if (ethEnviroment.equals(Enviroment.ethEnviromentType.ETHDEV_ENVIROMENT)) {
			ip = Enviroment.ethEnviromentType.ETHDEV_ENVIROMENT.getValue();
			Web3j web3j = Web3j.build(new HttpService(ip));
			System.out.println("the Dev of Eth connect success");
		} else if (ethEnviroment.equals(Enviroment.ethEnviromentType.ETHPRD_ENVIROMENT.getValue())) {
			ip = Enviroment.ethEnviromentType.ETHDEV_ENVIROMENT.getValue();
			Web3j web3j = Web3j.build(new HttpService(ip));
			System.out.println("the prd of Eth connect success");
		} else {
			System.out.println("please check out your address");
		}
	}

	// get web3j client
	private static Web3j getClient() {
		return web3j = Web3j.build(new HttpService(ip));
	}

	// get admin client
	private static Admin getAdmin() {
		admin = Admin.build(new HttpService(ip));
		return admin;
	}

	// get parity client
	private static Parity getParity() {
		Parity parity = Parity.build(new HttpService(ip));
		return parity;
	}

	// create addressַ
	@Override
	public String GenerateAddress(String password) {
		if (getClient() != null) {
			System.out.println("conect success " + getAdmin());
		}
		try {
			System.out.println("create address");
			Request<?, org.web3j.protocol.admin.methods.response.NewAccountIdentifier> personalNewAccount = getAdmin()
					.personalNewAccount(password);
			org.web3j.protocol.admin.methods.response.NewAccountIdentifier send = personalNewAccount.send();
			String address = send.getAccountId();
			System.out.println(address);
			return address;
		} catch (Exception e) {
			System.out.println("create error");
		}
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

	// 转账
	@Override
	public String Transfer(String to, String from, String privateKey, BigDecimal amount, BigDecimal gasPrice) {

		Transaction transaction = Transaction.createEtherTransaction(from, null, gasPrice.toBigInteger(), null, to,
				amount.toBigInteger());
		try {
			EthSendTransaction ethSendTransaction = getParity().personalSendTransaction(transaction, privateKey).send();
			if (ethSendTransaction != null) {
				String tradeHash = ethSendTransaction.getTransactionHash();
				System.out.println("hash:" + tradeHash);
				return tradeHash;
			}
		} catch (Exception e) {
			System.out.println("the trans error");
		}
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

	// 判断法币的合理性
	@Override
	public boolean leafOfAddress(String address) throws Exception {
		EthGetCode code = web3j.ethGetCode(address, DefaultBlockParameterName.LATEST).send();
		System.out.println(code.getCode());
		return false;
	}

	/*获取token信息*/
	/*查询token余额*/
	/*转账*/

}

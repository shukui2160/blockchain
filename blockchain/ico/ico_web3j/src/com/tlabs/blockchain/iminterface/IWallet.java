package com.tlabs.blockchain.iminterface;

import java.math.BigDecimal;

import org.web3j.protocol.core.DefaultBlockParameter;

import com.tlabs.blockchain.model.Enviroment;
import com.tlabs.blockchain.model.TransactionMange;

public interface IWallet {
	void Connection(Enviroment enviroment);// ����

	String GenerateAddress(String password);// ���ɵ�����ַ

	void GetWalletInfo();// ��ȡǮ����Ϣ�����˻�

	void BackupWallet();// ����˽Կ Ǯ������

	void OutputPrivateKeys();// ����˽Կ

	String Transfer(String to, String from,String privateKey,BigDecimal amount,BigDecimal gasPrice);

	void GetTXProcess();

	TransactionMange GetTXInfo();
	
	//判断地址是否合法
	boolean leafOfAddress(String address) throws Exception;
}

package com.minute.service.block.service;

import com.common.tookit.result.TlabsResult;

public interface AccountService {

	TlabsResult createAccount();
	
	TlabsResult createProjectAccount();

	TlabsResult getTransactionList(String address, String fromBlockNum);

	TlabsResult getContractTransactionList(String address, String fromBlockNum);

	TlabsResult ethTransfer(String fromAdd, String toAdd, String value, String gas);

	TlabsResult tokenTransfer(String fromAdd, String contractAdd, String toAdd, String value, String gas);

	TlabsResult getBalance(String address, String contraceAddress);

	TlabsResult getTokenInfo(String contractAdd);

	TlabsResult validateTransaction(String txHash);

	

}

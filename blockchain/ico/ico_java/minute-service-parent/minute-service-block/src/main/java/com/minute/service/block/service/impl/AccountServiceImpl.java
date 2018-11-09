package com.minute.service.block.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import com.common.tookit.coin.DecimalUtils;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.common.eth.EthTemplate;
import com.minute.service.block.common.BlockEnum;
//import com.minute.service.block.common.EthUtils;
import com.minute.service.block.common.TransactionEnum;
import com.minute.service.block.entry.EthAccount;
import com.minute.service.block.entry.EthBlock;
import com.minute.service.block.entry.EthTransaction;
import com.minute.service.block.mapper.EthAccountMapper;
import com.minute.service.block.mapper.EthBlockMapper;
import com.minute.service.block.mapper.EthTransactionMapper;
import com.minute.service.block.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private EthTemplate ethTemplate;

	@Autowired
	private EthAccountMapper ethAccountMapper;

	@Autowired
	private EthTransactionMapper ethTransactionMapper;

	@Autowired
	private EthBlockMapper ethBlockMapper;

	private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Override
	public TlabsResult createAccount() {
		// String passwd = EthUtils.getRandomPwd(32);
		String passwd = "123";
		String address = ethTemplate.createAccount(passwd);
		EthAccount account = new EthAccount();
		account.setAccountAddress(address);
		account.setAccountPasswd(passwd);
		ethAccountMapper.insertSelective(account);
		Map<String, Object> data = new HashMap<>();
		data.put("address", address);
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult createProjectAccount() {
		// String passwd = EthUtils.getRandomPwd(32);
		String passwd = "123";
		String address = ethTemplate.createAccount(passwd);
		EthAccount account = new EthAccount();
		account.setAccountAddress(address);
		account.setAccountPasswd(passwd);
		account.setType("1");// 项目账户
		ethAccountMapper.insertSelective(account);
		Map<String, Object> data = new HashMap<>();
		data.put("address", address);
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult getTransactionList(String address, String fromBlockNum) {
		// 获取已经遍历过 block列表的 最新块高度
		EthBlock block = ethBlockMapper.selectLatestBlock();
		Long latestBlockNum = block.getBlockNum();
		if (Long.valueOf(fromBlockNum) >= latestBlockNum) {
			// return 已同步充值记录到最新
		}
		List<EthTransaction> list = ethTransactionMapper.selectRechargeRecords(address, Long.valueOf(fromBlockNum),
				latestBlockNum);
		List<Map<String, Object>> trnsactionList = new ArrayList<>();
		for (EthTransaction ethTransaction : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("txHash", ethTransaction.getTxHash());
			map.put("fromAdd", ethTransaction.getFromAdd());
			map.put("toAdd", ethTransaction.getToAdd());
			map.put("value", ethTransaction.getValue());
			map.put("createTime", ethTransaction.getCreateTime());
			trnsactionList.add(map);
		}

		Map<String, Object> data = new HashMap<>();
		data.put("latestBlockNum", latestBlockNum);
		data.put("trnsactionList", trnsactionList);
		return ResultUtils.createSuccess(data);
	}

	// 合约账户的充值记录列表
	@Override
	public TlabsResult getContractTransactionList(String address, String fromBlockNum) {
		// 获取已经遍历过 block列表的 最新块高度
		EthBlock block = ethBlockMapper.selectLatestBlock();
		Long latestBlockNum = block.getBlockNum();
		if (Long.valueOf(fromBlockNum) >= latestBlockNum) {
			// return 已同步充值记录到最新
		}

		List<EthTransaction> list = ethTransactionMapper.selectRechargeRecords(address, Long.valueOf(fromBlockNum),
				latestBlockNum);
		List<Map<String, Object>> trnsactionList = new ArrayList<>();
		for (EthTransaction ethTransaction : list) {
			Map<String, Object> map = new HashMap<>();
			map.put("txHash", ethTransaction.getTxHash());
			map.put("fromAdd", ethTransaction.getFromAdd());
			map.put("value", ethTransaction.getValue());
			map.put("createTime", ethTransaction.getCreateTime());

			// 根据txid查询 真正的to地址

			trnsactionList.add(map);
		}

		Map<String, Object> data = new HashMap<>();
		data.put("latestBlockNum", latestBlockNum);
		data.put("trnsactionList", trnsactionList);
		return ResultUtils.createSuccess(data);
	}

	@SuppressWarnings("unused")
	private void getTransactionReceipt(String txHash) {
		TransactionReceipt record = ethTemplate.getTransactionReceiptByHash(txHash);
	}

	@Override
	public TlabsResult ethTransfer(String fromAdd, String toAdd, String value, String gas) {
		log.info("2--->fromAdd:" + fromAdd);
		log.info("2--->toAdd:" + toAdd);
		log.info("2--->value:" + value);
		log.info("2--->gas:" + gas);

		EthAccount ethAccount = ethAccountMapper.selectByAccountAddress(fromAdd);
		String passwd = ethAccount.getAccountPasswd();
		String txHash = ethTemplate.ethTransfer(fromAdd, passwd, toAdd, new BigInteger(value), gas);
		if (txHash != null) {
			Map<String, Object> data = new HashMap<>();
			data.put("txHash", txHash);
			return ResultUtils.createSuccess(data);
		} else {
			return ResultUtils.createResult(TransactionEnum.TRANSACTION_FAIL.getCode(),
					TransactionEnum.TRANSACTION_FAIL.getChMsg());
		}
	}

	@Override
	public TlabsResult tokenTransfer(String fromAdd, String contractAdd, String toAdd, String value, String gas) {
		log.info("1--->fromAdd:" + fromAdd);
		log.info("1--->contractAdd:" + contractAdd);
		log.info("1--->toAdd:" + toAdd);
		log.info("1--->value:" + value);
		log.info("1--->gas:" + gas);

		EthAccount ethAccount = ethAccountMapper.selectByAccountAddress(fromAdd);
		String passwd = ethAccount.getAccountPasswd();
		String txHash = ethTemplate.tokenTransfer(fromAdd, passwd, toAdd, contractAdd, new BigInteger(value), gas);
		if (txHash != null) {
			Map<String, Object> data = new HashMap<>();
			data.put("txHash", txHash);
			return ResultUtils.createSuccess(data);
		} else {
			return ResultUtils.createResult(TransactionEnum.TRANSACTION_FAIL.getCode(),
					TransactionEnum.TRANSACTION_FAIL.getChMsg());
		}
	}

	@Override
	public TlabsResult getBalance(String address, String contractAddress) {
		String coinNum = ethTemplate.ethGetBalance(address);
		String tokenNum = ethTemplate.tokenGetBalance(address, contractAddress);
		Map<String, Object> data = new HashMap<>();
		data.put("coinNum", coinNum);
		data.put("tokenNum", tokenNum);
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult getTokenInfo(String contractAdd) {
		log.info("合约地址:" + contractAdd);
		try {
			String tokenCode = ethTemplate.tokenGetSymbol(contractAdd);
			String circulation = ethTemplate.tokenGetTotalSupply(contractAdd).toString();
			String decimals = String.valueOf(ethTemplate.tokenGetDecimals(contractAdd));
			Map<String, String> data = new HashMap<>();
			data.put("tokenCode", tokenCode.toUpperCase());// 代币简称
			data.put("circulation", DecimalUtils.toToken(circulation, decimals));// 总发行量
			data.put("decimals", decimals);// 精确度
			return ResultUtils.createSuccess(data);
		} catch (Exception e) {
			return ResultUtils.createResult(BlockEnum.CONTRACT_ADDRESS_ERROR.getCode(),
					BlockEnum.CONTRACT_ADDRESS_ERROR.getChMsg());
		}

	}

	@Override
	public TlabsResult validateTransaction(String txHash) {
		TransactionReceipt transactionReceipt = ethTemplate.getTransactionReceiptByHash(txHash);
		if (transactionReceipt != null) {
			return ResultUtils.createSuccess();
		} else {
			return ResultUtils.createResult(BlockEnum.UN_CONFIRM_TRANSACTION.getCode(),
					BlockEnum.UN_CONFIRM_TRANSACTION.getChMsg());
		}
	}

}

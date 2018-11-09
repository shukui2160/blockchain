package com.minute.common.eth;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthSyncing;
import org.web3j.protocol.core.methods.response.EthSyncing.Result;
import org.web3j.protocol.core.methods.response.EthSyncing.Syncing;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import com.alibaba.fastjson.JSONObject;
import com.minute.common.eth.env.Environment;
import com.minute.common.eth.exception.TlabsEthException;
import com.minute.common.eth.utils.GasUtils;

public class EthTemplate {

	private Admin admin;

	private Web3j web3j;

	public EthTemplate(String url) {
		web3j = Web3j.build(new HttpService(url));
		admin = Admin.build(new HttpService(url));
	}

	/**
	 * 创建账户
	 * 
	 * @param passwd
	 *            账户密码
	 * @return
	 */
	public String createAccount(String passwd) {
		try {
			return admin.personalNewAccount(passwd).send().getAccountId();
		} catch (IOException e) {
			e.printStackTrace();
			throw new TlabsEthException("创建账户异常！");
		}
	}

	/**
	 * 查询账户余额
	 * 
	 * @param address
	 * @return
	 */
	public String ethGetBalance(String address) {
		try {
			EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
			return ethGetBalance.getBalance().toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new TlabsEthException("查询账户余额异常！");
		}
	}

	/**
	 * 解锁账户
	 * 
	 * @param address
	 * @param passwd
	 */
	private void unlockAccount(String address, String passwd) {
		BigInteger unlockDuration = BigInteger.valueOf(60L);
		try {
			admin.personalUnlockAccount(address, passwd, unlockDuration).send();
		} catch (IOException e) {
			e.printStackTrace();
			throw new TlabsEthException("解锁账户异常！");
		}
	}

	/**
	 * 获取账户 在当前节点交易次数, 依次递增 中间不能中断
	 * 
	 * @param address
	 * @return
	 */
	private BigInteger getOnece(String address) {
		try {
			// EthGetTransactionCount count = web3j.ethGetTransactionCount(address,
			// DefaultBlockParameterName.LATEST)
			// .sendAsync().get();

			// 解决 一个账户不能频繁交易的问题
			EthGetTransactionCount count = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING)
					.sendAsync().get();
			return count.getTransactionCount();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取账户onece异常！");
		}
	}

	/**
	 * 以太币转账
	 * 
	 * @param from
	 *            账户地址
	 * @param passwd
	 *            from对应的密码
	 * @param to
	 *            账户地址
	 * @param value
	 *            转账金额 单位wei
	 * @return null 或者 txid ; 注意超时问题
	 */
	public String ethTransfer(String from, String passwd, String to, BigInteger value,String gas) {
		BigInteger nonce = getOnece(from);
		// System.out.println(nonce);
		// BigInteger gasPrice = Contract.GAS_PRICE;
		// BigInteger gasLimit = Contract.GAS_LIMIT.divide(new BigInteger("2"));
		BigInteger gasPrice = GasUtils.getGasPrice();
		BigInteger gasLimit = GasUtils.getGasLimit();
		try {
			unlockAccount(from, passwd);
			Transaction transaction = Transaction.createEtherTransaction(from, nonce, gasPrice, gasLimit, to, value);
			EthSendTransaction transactionResponse = web3j.ethSendTransaction(transaction).sendAsync().get();
			return transactionResponse.getTransactionHash();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TlabsEthException("以太币转账异常！");
		}
	}

	// 获取最新区块高度 highestBlock
	public Long getLatestBlockNum() {
		try {
			BigInteger num = web3j.ethBlockNumber().send().getBlockNumber();
			return num.longValue();
		} catch (IOException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取Block高度异常！");
		}
	}

	// 获取本地节点 已同步区块高度 currentBlock
	public Long getCurrentBlockNum() {
		try {
			// BigInteger num = web3j.ethBlockNumber().send().getBlockNumber();
			EthSyncing ethSyncing = web3j.ethSyncing().send();
			Result result = ethSyncing.getResult();
			if (!result.isSyncing()) {
				return null;
			}
			String num = ((Syncing) result).getCurrentBlock();
			return Long.parseLong(num.substring(2), 16);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取Block高度异常！");
		}
	}

	/**
	 * 根据blockNum 查询交易列表
	 * 
	 * @param blockNum
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<TransactionResult> getTranctionListByBlockNum(String blockNum) {
		DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(blockNum));
		try {
			Block block = admin.ethGetBlockByNumber(defaultBlockParameter, true).send().getBlock();
			List<TransactionResult> transactions = block.getTransactions();
			return transactions;
		} catch (IOException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取Block交易列表异常！");
		}
	}

	/**
	 * 根据交易hash获取交易详情
	 * 
	 * @param hash
	 * @return
	 * @throws IOException
	 */
	public EthTransaction getTransactionByHash(String hash) {
		try {
			Request<?, EthTransaction> request = web3j.ethGetTransactionByHash(hash);
			return request.send();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TlabsEthException("获取transaction详情异常!");
		}

	}

	/**
	 * 根据交易hash判断交易是否完成
	 * 
	 * @param hash
	 * @return null或者不为null 当交易未被确认的时候为null
	 * @throws IOException
	 */
	public TransactionReceipt getTransactionReceiptByHash(String hash) {
		try {
			EthGetTransactionReceipt record = web3j.ethGetTransactionReceipt(hash).send();
			return record.getResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new TlabsEthException("获取transaction Receipt详情异常！");
		}

	}

	/**
	 * 查询 代币余额
	 * 
	 * @param fromAddress
	 *            token账户地址
	 * @param contractAddress
	 *            合约地址
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String tokenGetBalance(String fromAddress, String contractAddress) {
		String methodName = "balanceOf";
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		Address address = new Address(fromAddress);
		inputParameters.add(address);

		TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
		};
		outputParameters.add(typeReference);
		Function function = new Function(methodName, inputParameters, outputParameters);
		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);
		EthCall ethCall;
		try {
			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			return results.get(0).getValue().toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取token余额异常！");
		}
	}

	/**
	 * 获取代币名字
	 * 
	 * @param contractAddress
	 *            合约地址
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String tokenGetName(String contractAddress) {
		String methodName = "name";
		String fromAddr = Environment.EMPTY_ADDRESS;
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
		};
		outputParameters.add(typeReference);
		Function function = new Function(methodName, inputParameters, outputParameters);

		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			return results.get(0).getValue().toString();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取token Name异常！");
		}
	}

	/**
	 * 查询代币简称
	 * 
	 * @param contractAddress
	 *            合约地址
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String tokenGetSymbol(String contractAddress) {
		String methodName = "symbol";
		String fromAddr = Environment.EMPTY_ADDRESS;
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();

		TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
		};
		outputParameters.add(typeReference);
		Function function = new Function(methodName, inputParameters, outputParameters);
		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
		try {
			EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			return results.get(0).getValue().toString();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取token Name异常！");
		}

	}

	/**
	 * 查询代币精度
	 *
	 * @param web3j
	 * @param contractAddress
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int tokenGetDecimals(String contractAddress) {
		String methodName = "decimals";
		String fromAddr = Environment.EMPTY_ADDRESS;
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();

		TypeReference<Uint8> typeReference = new TypeReference<Uint8>() {
		};
		outputParameters.add(typeReference);

		Function function = new Function(methodName, inputParameters, outputParameters);

		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

		EthCall ethCall;
		try {
			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			int decimal = Integer.parseInt(results.get(0).getValue().toString());
			return decimal;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取token精度异常！");
		}

	}

	/**
	 * 查询代币发行总量
	 *
	 * @param web3j
	 * @param contractAddress
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BigInteger tokenGetTotalSupply(String contractAddress) {
		String methodName = "totalSupply";
		String fromAddr = Environment.EMPTY_ADDRESS;
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();

		TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
		};
		outputParameters.add(typeReference);

		Function function = new Function(methodName, inputParameters, outputParameters);

		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);

		EthCall ethCall;
		try {
			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			BigInteger totalSupply = (BigInteger) results.get(0).getValue();
			return totalSupply;
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			throw new TlabsEthException("获取token精度异常！");
		}
	}

	/**
	 * 
	 * @param fromAddress
	 * @param password
	 * @param toAddress
	 * @param contractAddress
	 * @param amount
	 * @return null 或者 txid ;注意timeout问题
	 */
	@SuppressWarnings("rawtypes")
	public String tokenTransfer(String fromAddress, String password, String toAddress, String contractAddress,
			BigInteger amount,String gas) {
		String txHash = null;
		try {
			PersonalUnlockAccount personalUnlockAccount = admin
					.personalUnlockAccount(fromAddress, password, BigInteger.valueOf(10)).send();
			if (personalUnlockAccount == null) {
				throw new TlabsEthException("无效的账户地址！");
			}
			if (personalUnlockAccount.accountUnlocked()) {
				String methodName = "transfer";
				List<Type> inputParameters = new ArrayList<>();
				List<TypeReference<?>> outputParameters = new ArrayList<>();

				Address tAddress = new Address(toAddress);

				Uint256 value = new Uint256(amount);
				inputParameters.add(tAddress);
				inputParameters.add(value);

				TypeReference<Bool> typeReference = new TypeReference<Bool>() {
				};
				outputParameters.add(typeReference);

				Function function = new Function(methodName, inputParameters, outputParameters);

				String data = FunctionEncoder.encode(function);

				EthGetTransactionCount ethGetTransactionCount = web3j
						.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING).sendAsync().get();
				BigInteger nonce = ethGetTransactionCount.getTransactionCount();

				// BigInteger gasPrice =
				// Convert.toWei(BigDecimal.valueOf(5),Convert.Unit.GWEI).toBigInteger();
				// BigInteger gasLimit = BigInteger.valueOf(60000);

				BigInteger gasPrice = GasUtils.getGasPrice();
				BigInteger gasLimit = GasUtils.getGasLimit();

				Transaction transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice,
						gasLimit, contractAddress, data);
				EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).sendAsync().get();
				System.out.println(JSONObject.toJSONString(ethSendTransaction));
				txHash = ethSendTransaction.getTransactionHash();
			}
			return txHash;
		} catch (Exception e) {
			e.printStackTrace();
			throw new TlabsEthException("token转账异常！");
		}
	}
}

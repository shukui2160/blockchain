package com.minute.service.block.service.impl;

import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionObject;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;

import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.common.eth.EthTemplate;
import com.minute.service.block.common.Dic4Block;
import com.minute.service.block.entry.EthAccount;
import com.minute.service.block.entry.EthBlock;
import com.minute.service.block.entry.EthIncome;
import com.minute.service.block.entry.EthTransaction;
import com.minute.service.block.mapper.EthAccountMapper;
import com.minute.service.block.mapper.EthBlockMapper;
import com.minute.service.block.mapper.EthIncomeMapper;
import com.minute.service.block.mapper.EthTokenMapper;
import com.minute.service.block.mapper.EthTransactionMapper;
import com.minute.service.block.remote.WalletRemoteService;
import com.minute.service.block.service.BlockService;
import cn.hutool.core.util.RandomUtil;

@Service
@Transactional
public class BlockServiceImpl implements BlockService {

	@Autowired
	private EthBlockMapper ethBlockMapper;

	@Autowired
	private EthTransactionMapper ethTransactionMapper;

	@Autowired
	private EthAccountMapper ethAccountMapper;

	@Autowired
	private EthTokenMapper ethTokenMapper;

	@Autowired
	private EthIncomeMapper ethIncomeMapper;

	@Autowired
	private EthTemplate ethTemplate;

	@Autowired
	private WalletRemoteService walletRemoteService;

	private static final Logger log = LoggerFactory.getLogger(BlockServiceImpl.class);

	@Override
	public TlabsResult getBlockList() {
		Long num = null;
		// 测试链 获取不到 num
		num = ethTemplate.getCurrentBlockNum();
		if (num == null) {
			num = ethTemplate.getLatestBlockNum();
		}
		log.info("block高度为" + num);
		List<EthBlock> list = ethBlockMapper.selectByStatus(num, Dic4Block.EthBlockSatatus.UN_SYN.getKey());
		return ResultUtils.createSuccess(list);
	}

	@Async
	@Override
	@SuppressWarnings("rawtypes")
	public void synBlock(String blockNum) {
		log.info("同步数据块中:" + blockNum);
		List<TransactionResult> list = ethTemplate.getTranctionListByBlockNum(blockNum);
		for (TransactionResult transactionResult : list) {
			TransactionObject tx = (TransactionObject) transactionResult;
			EthTransaction transaction = new EthTransaction();
			transaction.setBlockNum(Long.valueOf(blockNum));
			transaction.setTxHash(tx.getHash());
			transaction.setFromAdd(tx.getFrom());
			transaction.setToAdd(tx.getTo());
			transaction.setValue(tx.getValue().toString());
			ethTransactionMapper.insertSelective(transaction);

			// 如果to地址是平台账户地址
			EthAccount ethAccount = ethAccountMapper.selectByAccountAddress(tx.getTo());
			if (ethAccount != null) {
				EthIncome income = new EthIncome();
				income.setDealId(RandomUtil.randomUUID());
				income.setTxHash(tx.getHash());
				income.setFromAdd(tx.getFrom());
				income.setToAdd(tx.getTo());
				income.setTransactionType("0"); // 法币相关交易
				ethIncomeMapper.insertSelective(income);
			} else if (ethTokenMapper.selectByContractAddress(tx.getTo()) != null) {
				EthIncome income = new EthIncome();
				income.setDealId(RandomUtil.randomUUID());
				income.setTxHash(tx.getHash());
				income.setFromAdd(tx.getFrom());
				income.setToAdd(tx.getTo());
				income.setTransactionType("1"); // 代币相关交易
				ethIncomeMapper.insertSelective(income);
			} else {
				log.info("do nothing~~");
			}
		}

		// 更改block状态为已经同步
		EthBlock block = ethBlockMapper.selectByblockNum(Long.valueOf(blockNum));
		block.setStatus(Dic4Block.EthBlockSatatus.SYN.getKey());
		block.setUpdateTime(new Date());
		ethBlockMapper.updateByPrimaryKey(block);
	}

	@Override
	public TlabsResult initBlock(Long fromNum, Long toNum) {
		for (long i = fromNum; i < toNum; i++) {
			EthBlock block = new EthBlock();
			block.setBlockNum(i);
			ethBlockMapper.insertSelective(block);
		}
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult notifyWallet() {
		List<EthIncome> records = ethIncomeMapper.selectByStatus("0");
		// 遍历需要确认的 记录
		for (EthIncome record : records) {
			if ("0".equals(record.getTransactionType())) {
				// 普通账户交易 可能是用户相关 也可能是项目相关
				EthAccount account = ethAccountMapper.selectByAccountAddress(record.getToAdd());
				notifyUser(record, account);
			} else {
				// 合约账户 交易
				// 项目相关-项目地址冲代币
			}
		}
		return ResultUtils.createSuccess();
	}

	private void notifyUser(EthIncome record, EthAccount account) {
		try {
			TlabsResult walletResult = null;
			if ("0".equals(account.getType())) {
				// 0代表是个人地址
				// 通知 wallet service 生成 收入账单
				String value = ethTemplate.getTransactionByHash(record.getTxHash()).getResult().getValue().toString();
				walletResult = walletRemoteService.coinRecharge(record.getDealId(), record.getTxHash(),
						record.getToAdd(), value);
			} else {
				// 1 代表项目地址
				// 通知 wallet service 生成 充币记录
			}
			// 更改记录状态
			log.info("-------------->" + JSONObject.toJSONString(walletResult));
			if (walletResult != null && walletResult.isSuccess()) {
				record.setStatus("1");
				record.setUpdateTime(new Date());
				ethIncomeMapper.updateByPrimaryKeySelective(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("通知钱包服务失败！");
		}
	}

}

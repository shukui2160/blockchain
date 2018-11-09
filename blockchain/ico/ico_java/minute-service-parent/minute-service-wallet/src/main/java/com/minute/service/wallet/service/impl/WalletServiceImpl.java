package com.minute.service.wallet.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.common.tookit.coin.DecimalUtils;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.common.Dic4UserRecord;
import com.minute.service.wallet.common.Dic4Wallet;
import com.minute.service.wallet.common.WalletEnum;
import com.minute.service.wallet.entry.Coin;
import com.minute.service.wallet.entry.Token;
import com.minute.service.wallet.entry.UserCoin;
import com.minute.service.wallet.entry.UserRecord;
import com.minute.service.wallet.entry.UserWallet;
import com.minute.service.wallet.mapper.CoinMapper;
import com.minute.service.wallet.mapper.TokenMapper;
import com.minute.service.wallet.mapper.UserCoinMapper;
import com.minute.service.wallet.mapper.UserRecordMapper;
import com.minute.service.wallet.mapper.UserWalletMapper;
import com.minute.service.wallet.remote.BlockRemoteService;
import com.minute.service.wallet.service.WalletService;

@Service
@Transactional
public class WalletServiceImpl implements WalletService {

	@Autowired
	private UserWalletMapper userWalletMapper;

	@Autowired
	private UserCoinMapper userCoinMapper;

	@Autowired
	private BlockRemoteService blockRemoteService;

	@Autowired
	private TokenMapper tokenMapper;

	@Autowired
	private UserRecordMapper userRecordMapper;

	@Autowired
	private CoinMapper coinMapper;

	private static final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

	@Override
	public TlabsResult initWallet(String userId) {
		if (userWalletMapper.selectByUserId(Long.valueOf(userId)) != null) {
			return ResultUtils.createSuccess();
		}
		// 创建钱包信息
		UserWallet wallet = new UserWallet();
		wallet.setUserId(Long.valueOf(userId));
		wallet.setType(Dic4Wallet.WalletType.USER_WALLET.getKey());
		userWalletMapper.insertSelective(wallet);

		// 创建以太币账户
		TlabsResult address = this.blockRemoteService.batchMakeAccount();
		if (!address.isSuccess()) {
			return address;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) address.getDate();
		UserCoin ethCoin = new UserCoin();
		ethCoin.setWalletId(wallet.getId());
		ethCoin.setCoinTokenId(2L);
		ethCoin.setAccountAdd(map.get("address"));
		ethCoin.setType(Dic4Wallet.WalletCoinType.COIN.getKey());
		ethCoin.setCoinTokenCode("ETH");
		userCoinMapper.insertSelective(ethCoin);

		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult selectCoin(String userId, Long coinTokenId, String type) {
		UserWallet wallet = userWalletMapper.selectByUserId(Long.valueOf(userId));
		UserCoin coin = userCoinMapper.selectByWalletId(wallet.getId(), coinTokenId, type);
		Map<String, Object> data = new HashMap<>();
		data.put("id", coin.getId());
		data.put("walletId", coin.getWalletId());
		data.put("coinTokenId", coin.getCoinTokenId());
		data.put("type", coin.getType());
		data.put("accountAdd", coin.getAccountAdd());
		data.put("accountNum", coin.getAccountNum());
		data.put("accountAvailableNum", coin.getAccountAvailableNum());
		data.put("accountLockNum", coin.getAccountLockNum());
		return ResultUtils.createSuccess(data);
	}

	@Override
	public void initTokenAccount(String userId, Long coinId, Long tokenId) {
		UserWallet wallet = userWalletMapper.selectByUserId(Long.valueOf(userId));
		UserCoin tokenCoin = userCoinMapper.selectByWalletId(wallet.getId(), tokenId,
				Dic4Wallet.WalletCoinType.TOKEN.getKey());
		if (tokenCoin == null) {
			Token token = tokenMapper.selectByPrimaryKey(tokenId);
			UserCoin coin = userCoinMapper.selectByWalletId(wallet.getId(), Long.valueOf(coinId),
					Dic4Wallet.WalletCoinType.COIN.getKey());
			UserCoin tokenAccount = new UserCoin();
			// 代币账户地址 共享以太坊地址
			tokenAccount.setWalletId(coin.getWalletId());
			tokenAccount.setCoinTokenId(tokenId);
			tokenAccount.setCoinTokenCode(token.getTokenCode());
			tokenAccount.setType(Dic4Wallet.WalletCoinType.TOKEN.getKey());
			tokenAccount.setAccountAdd(coin.getAccountAdd());
			userCoinMapper.insertSelective(tokenAccount);
		}
	}

	@Override
	public TlabsResult pay4project(String userId, Long coinId, String dealId, String payNum, String tokenNum,
			String languageId, String projectAdd, Long tokenId) {
		log.info("---->userId:" + userId);
		log.info("---->coinId:" + coinId);
		log.info("---->dealId:" + dealId);
		log.info("---->payNum:" + payNum);
		log.info("---->tokenNum:" + tokenNum);
		log.info("---->languageId:" + languageId);
		log.info("---->projectAdd:" + projectAdd);
		log.info("---->tokenId:" + tokenId);

		// 查询以太坊账户 判断可用余额是否够用 (极端条件需要 行级锁)
		UserWallet wallet = userWalletMapper.selectByUserId(Long.valueOf(userId));
		UserCoin userCoin = userCoinMapper.selectByWalletIdForUpdate(wallet.getId(), Long.valueOf(coinId),
				Dic4Wallet.WalletCoinType.COIN.getKey());

		Coin coin = coinMapper.selectByPrimaryKey(Long.valueOf(coinId));
		String payNumWithGas = DecimalUtils.add(coin.getDefalutGas(), payNum);
		if (!DecimalUtils.compare(userCoin.getAccountNum(), payNumWithGas)) {
			// return 余额不足
			return ResultUtils.createResult(WalletEnum.NO_BALANCE.getCode(), WalletEnum.NO_BALANCE.getMsg(languageId));
		}
		// 以太坊账户 账户总额 减去 payNumWithGas
		userCoin.setAccountNum(DecimalUtils.subtract(userCoin.getAccountNum(), payNumWithGas));
		userCoin.setUpdateTime(new Date());
		userCoinMapper.updateByPrimaryKeySelective(userCoin);

		// 代币账户 总额 加上 tokenNum
		UserCoin tokenCoinRecord = userCoinMapper.selectByWalletIdForUpdate(wallet.getId(), tokenId,
				Dic4Wallet.WalletCoinType.TOKEN.getKey());
		tokenCoinRecord.setAccountNum(DecimalUtils.add(tokenCoinRecord.getAccountNum(), tokenNum));
		tokenCoinRecord.setUpdateTime(new Date());
		userCoinMapper.updateByPrimaryKeySelective(tokenCoinRecord);

		// 链上交互
		Token token = tokenMapper.selectByPrimaryKey(tokenId);
		String contractAddress = token.getTokenAdd();
		String gas = DecimalUtils.toWei(coin.getDefalutGas());

		// 调用block service 实现token转账 获取txHash 并生成 流水记录
		log.info("---->userId:" + "调用block service 实现token转账 获取txHash 并生成 流水记录");
		String tokenValue = DecimalUtils.multiply(tokenNum, token.getDecimals());
		TlabsResult tokenResult = blockRemoteService.tokenTransfer(projectAdd, contractAddress,
				tokenCoinRecord.getAccountAdd(), DecimalUtils.removeZero(tokenValue), gas);
		log.info("---->" + JSONObject.toJSONString(tokenResult));
		if (!tokenResult.isSuccess()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动事务回滚
			return tokenResult;
		}
		String tokenTxHash = JSONObject.parseObject(JSONObject.toJSONString(tokenResult.getDate())).getString("txHash");

		
		// 产生代币转账记录
		UserRecord tokenRecord = new UserRecord();
		tokenRecord.setDealId(dealId);
		tokenRecord.setTxHash(tokenTxHash);
		tokenRecord.setUserId(Long.valueOf(userId));
		tokenRecord.setUserCoinId(tokenCoinRecord.getId());
		tokenRecord.setCoinTokenCode(token.getTokenCode());
		tokenRecord.setOneLeaveType(Dic4UserRecord.ONE_LEAVE_TYPE.IN_PUT.getKey());
		tokenRecord.setTwoLeaveType(Dic4UserRecord.TWO_LEAVE_TYPE.JOIN_PROJECT.getKey());
		tokenRecord.setTypeNote(Dic4UserRecord.TWO_LEAVE_TYPE.JOIN_PROJECT.getValue());
		tokenRecord.setValue(tokenNum);
		tokenRecord.setStatus(Dic4UserRecord.STATUSA.RUNNING.getKey());
		userRecordMapper.insertSelective(tokenRecord);

		// 调用block service 实现法币转账 获取txHash 并生成 流水记录
		log.info("---->" + "调用block service 实现法币转账 获取txHash 并生成 流水记录");
		String value = DecimalUtils.toWei(payNum); // 转化为单位 wei
		TlabsResult coinResult = blockRemoteService.ethTransfer(tokenCoinRecord.getAccountAdd(), projectAdd, value,
				gas);
		log.info("---->" + JSONObject.toJSONString(coinResult));
		if (!coinResult.isSuccess()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 手动事务回滚
			return coinResult;
		}
		String coinTxHash = JSONObject.parseObject(JSONObject.toJSONString(coinResult.getDate())).getString("txHash");
		log.info("---->coinTxHash:" + coinTxHash);

		// 产生法币转账记录
		UserRecord coinRecord = new UserRecord();
		coinRecord.setDealId(dealId);
		coinRecord.setTxHash(coinTxHash);
		coinRecord.setUserId(Long.valueOf(userId));
		coinRecord.setUserCoinId(userCoin.getId());
		coinRecord.setCoinTokenCode("ETH");
		coinRecord.setOneLeaveType(Dic4UserRecord.ONE_LEAVE_TYPE.OUT_PUT.getKey());
		coinRecord.setTwoLeaveType(Dic4UserRecord.TWO_LEAVE_TYPE.JOIN_PROJECT.getKey());
		coinRecord.setTypeNote(Dic4UserRecord.TWO_LEAVE_TYPE.JOIN_PROJECT.getValue());
		coinRecord.setValue(DecimalUtils.removeZero(payNumWithGas));
		coinRecord.setStatus(Dic4UserRecord.STATUSA.RUNNING.getKey());
		Map<String, String> note = new HashMap<>();
		note.put("gas", coin.getDefalutGas());
		coinRecord.setRecordInfo(JSONObject.toJSONString(note));
		userRecordMapper.insertSelective(coinRecord);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult selectCoinsByUserId(String userId) {
		List<Map<String, Object>> data = new ArrayList<>();
		UserWallet wallet = userWalletMapper.selectByUserId(Long.valueOf(userId));
		List<UserCoin> lsit = userCoinMapper.selectCoinsByWalletId(wallet.getId());
		for (UserCoin coin : lsit) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", coin.getId());
			map.put("coinTokenId", coin.getCoinTokenId());
			map.put("type", coin.getType());
			map.put("accountAdd", coin.getAccountAdd());
			map.put("accountNum", coin.getAccountNum());
			map.put("accountAvaliableNum", coin.getAccountAvailableNum());
			map.put("createTime", coin.getCreateTime());
			map.put("coinTokenCode", coin.getCoinTokenCode());
			data.add(map);
		}
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult selectUserCoinById(String userCoinId) {
		UserCoin coin = userCoinMapper.selectByPrimaryKey(Long.valueOf(userCoinId));
		return ResultUtils.createSuccess(coin);
	}

	@Override
	public TlabsResult selectBillsByUserCoinId(Long userId, Long userCoinId,String type) {
		// TODO Auto-generated method stub
		List<UserRecord> selectBillsByUserCoinId = this.userRecordMapper.selectBillsByUserCoinIdType(userId, userCoinId, type);
		List<Map<String, Object>> data = new ArrayList<>();
		int valueNum = 0;
		for (UserRecord userRecord : selectBillsByUserCoinId) {
			valueNum += Integer.parseInt(userRecord.getValue());
		}
		for (UserRecord userRecord : selectBillsByUserCoinId) {
			Map<String, Object> map = new HashMap<>();
			map.put("valueNum", valueNum);
			map.put("createTime", userRecord.getCreateTime());
			map.put("type", userRecord.getTypeNote());
			map.put("coinTokenType", userRecord.getCoinTokenCode());
			map.put("value", userRecord.getValue());
			data.add(map);
		}
		
		return ResultUtils.createSuccess(data);
	}

}

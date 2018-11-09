package com.minute.service.wallet.service.impl;

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
import com.github.pagehelper.PageHelper;
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
import com.minute.service.wallet.service.CoinRecordService;

@Service
@Transactional
public class CoinRecordServiceImpl implements CoinRecordService {

	@Autowired
	private UserRecordMapper userRecordMapper;

	@Autowired
	private CoinMapper coinMapper;

	@Autowired
	private UserCoinMapper userCoinMapper;

	@Autowired
	private TokenMapper tokenMapper;

	@Autowired
	private BlockRemoteService blockRemoteService;

	@Autowired
	private UserWalletMapper userWalletMapper;

	private static final Logger log = LoggerFactory.getLogger(CoinRecordServiceImpl.class);

	@Override
	public TlabsResult listBills(Long userId, Long userCoinId, String index) {
		PageHelper.startPage(Integer.valueOf(index), 10, false);
		List<UserRecord> recordList = userRecordMapper.selectBillsByUserCoinId(userId, userCoinId);
		return ResultUtils.createSuccess(recordList);
	}

	@Override
	public TlabsResult bileInfo(String userId, String userRecordId) {
		UserRecord record = userRecordMapper.selectByPrimaryKey(Long.valueOf(userRecordId));
		if (userId.equals(record.getUserId().toString())) {
			return ResultUtils.createSuccess(record);
		} else {
			return ResultUtils.createException();
		}
	}

	@Override
	public TlabsResult getCoinInfo(String coinId) {
		Coin coin = coinMapper.selectByPrimaryKey(Long.valueOf(coinId));
		return ResultUtils.createSuccess(coin);
	}

	@Override
	public TlabsResult drawCoinToken(String userId, String dealId, String userCoinId, String toAddress, String value,
			String languageId) {
		// 根据userCoinId查询账户 并判断账户类型
		UserCoin userCoin = userCoinMapper.selectByPrimaryKey(Long.valueOf(userCoinId));
		Coin coin = coinMapper.selectByPrimaryKey(2L);// 2代表以太币·
		String gas = coin.getDefalutGas();
		if ("0".equals(userCoin.getType())) {
			// 法币账户
			UserCoin userCoin4Coin = userCoinMapper.selectByWalletIdForUpdate(userCoin.getWalletId(),
					userCoin.getCoinTokenId(), userCoin.getType());
			if (DecimalUtils.compareTT(value, userCoin4Coin.getAccountNum())) {
				// return 余额不足
				log.info("余额不足:01");
				return ResultUtils.createResult(WalletEnum.NO_BALANCE.getCode(), WalletEnum.NO_BALANCE.getChMsg());
			}
			// 法币账户扣款
			userCoin4Coin.setAccountNum(DecimalUtils.subtract(userCoin4Coin.getAccountNum(), value));
			userCoin4Coin.setUpdateTime(new Date());
			userCoinMapper.updateByPrimaryKey(userCoin4Coin);
			// 生成提现记录， 状态为 进行中
			UserRecord userRecord = new UserRecord();
			userRecord.setDealId(dealId);
			userRecord.setTxHash(null);// 还未开始支付
			userRecord.setUserCoinId(userCoin.getId());
			userRecord.setUserId(Long.valueOf(userId));
			userRecord.setCoinTokenCode("ETH");
			userRecord.setOneLeaveType(Dic4UserRecord.ONE_LEAVE_TYPE.OUT_PUT.getKey());
			userRecord.setTwoLeaveType(Dic4UserRecord.TWO_LEAVE_TYPE.DRAW_COIN.getKey());
			userRecord.setTypeNote(Dic4UserRecord.TWO_LEAVE_TYPE.DRAW_COIN.getValue());
			userRecord.setValue(value);
			userRecord.setStatus(Dic4UserRecord.STATUSA.RUNNING.getKey());
			Map<String, String> note = new HashMap<>();
			note.put("gas", gas);
			userRecord.setRecordInfo(JSONObject.toJSONString(note));
			userRecordMapper.insertSelective(userRecord);
			return ResultUtils.createSuccess();
		} else {
			// 代币账户
			UserCoin userCoin4Token = userCoinMapper.selectByWalletIdForUpdate(userCoin.getWalletId(),
					userCoin.getCoinTokenId(), userCoin.getType());
			if (DecimalUtils.compareTT(value, userCoin4Token.getAccountNum())) {
				// return token余额不足
				log.info("余额不足:02");
				return ResultUtils.createResult(WalletEnum.NO_BALANCE.getCode(), WalletEnum.NO_BALANCE.getChMsg());
			}
			// 代币账户扣款
			userCoin4Token.setAccountNum(DecimalUtils.subtract(userCoin4Token.getAccountNum(), value));
			userCoin4Token.setUpdateTime(new Date());
			userCoinMapper.updateByPrimaryKey(userCoin4Token);

			// 查询以太币账户 余额是否充足-是否够支撑gas值
			UserCoin userCoin4Coin = userCoinMapper.selectByWalletIdForUpdate(userCoin.getWalletId(), 2L,
					Dic4Wallet.WalletCoinType.COIN.getKey());
			if (DecimalUtils.compareTT(gas, userCoin4Coin.getAccountNum())) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				// return ETH余额不足
				log.info("余额不足:03");
				return ResultUtils.createResult(WalletEnum.NO_BALANCE.getCode(), WalletEnum.NO_BALANCE.getChMsg());
			}
			userCoin4Coin.setAccountNum(DecimalUtils.subtract(userCoin4Coin.getAccountNum(), gas));
			userCoin4Coin.setUpdateTime(new Date());
			userCoinMapper.updateByPrimaryKey(userCoin4Coin);

			Token token = tokenMapper.selectByPrimaryKey(userCoin.getCoinTokenId());

			// 生成提现记录
			// 生成提现记录， 状态为 进行中
			UserRecord userRecord = new UserRecord();
			userRecord.setDealId(dealId);
			userRecord.setTxHash(null);// 还未开始支付
			userRecord.setUserCoinId(userCoin.getId());
			userRecord.setUserId(Long.valueOf(userId));
			userRecord.setCoinTokenCode(token.getTokenCode());
			userRecord.setOneLeaveType(Dic4UserRecord.ONE_LEAVE_TYPE.OUT_PUT.getKey());
			userRecord.setTwoLeaveType(Dic4UserRecord.TWO_LEAVE_TYPE.DRAW_TOKIN.getKey());
			userRecord.setTypeNote(Dic4UserRecord.TWO_LEAVE_TYPE.DRAW_TOKIN.getValue());
			userRecord.setValue(value);
			userRecord.setStatus(Dic4UserRecord.STATUSA.RUNNING.getKey());
			Map<String, String> note = new HashMap<>();
			note.put("gas", gas);
			userRecord.setRecordInfo(JSONObject.toJSONString(note));
			userRecordMapper.insertSelective(userRecord);
			return ResultUtils.createSuccess();
		}
	}

	@Override
	public TlabsResult agreeDrawCoinToken(String dealId, String toAddress) {
		UserRecord userRecord = userRecordMapper.selectByDealId(dealId);
		UserCoin userCoin = userCoinMapper.selectByPrimaryKey(userRecord.getUserCoinId());
		String fromAddress = userCoin.getAccountAdd();

		// 法币账户
		if ("0".equals(userCoin.getType())) {
			String tempGas = JSONObject.parseObject(userRecord.getRecordInfo()).getString("gas");
			String gas = DecimalUtils.toWei(tempGas);
			String tempEthValue = DecimalUtils.subtract(userRecord.getValue(), tempGas);
			String ethValue = DecimalUtils.toWei(tempEthValue);
			TlabsResult blockResult = blockRemoteService.ethTransfer(fromAddress, toAddress, ethValue, gas);
			if (!blockResult.isSuccess()) {
				return blockResult;
			}
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(blockResult.getDate()));
			userRecord.setTxHash(data.getString("txHash"));
			userRecord.setUpdateTime(new Date());
			userRecordMapper.updateByPrimaryKey(userRecord);
			return ResultUtils.createSuccess();

		} else {
			// 代币账户
			String tempGas = JSONObject.parseObject(userRecord.getRecordInfo()).getString("gas");
			String gas = DecimalUtils.toWei(tempGas);
			Token token = tokenMapper.selectByPrimaryKey(userCoin.getCoinTokenId());
			String tokenValue = DecimalUtils.multiply(userRecord.getValue(), token.getDecimals());
			String contraceAddress = token.getTokenAdd();
			TlabsResult blockResult = blockRemoteService.tokenTransfer(fromAddress, contraceAddress, toAddress,
					tokenValue, gas);
			if (!blockResult.isSuccess()) {
				return blockResult;
			}
			JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(blockResult.getDate()));
			userRecord.setTxHash(data.getString("txHash"));
			userRecord.setUpdateTime(new Date());
			userRecordMapper.updateByPrimaryKey(userRecord);
			return ResultUtils.createSuccess();
		}
	}

	@Override
	public TlabsResult refuseDrawCoinToken(String dealId, String toAddress) {
		UserRecord userRecord = userRecordMapper.selectByDealId(dealId);
		UserCoin userCoin = userCoinMapper.selectByPrimaryKey(userRecord.getUserCoinId());
		// 法币账户
		if ("0".equals(userCoin.getType())) {
			UserCoin coin = userCoinMapper.selectByWalletIdForUpdate(userCoin.getWalletId(), userCoin.getCoinTokenId(),
					userCoin.getType());
			coin.setAccountNum(DecimalUtils.add(coin.getAccountNum(), userRecord.getValue()));
			coin.setUpdateTime(new Date());
			userCoinMapper.updateByPrimaryKey(coin);
		} else {
			// 代币账户加上gas值
			UserCoin tokenCoin = userCoinMapper.selectByWalletIdForUpdate(userCoin.getWalletId(),
					userCoin.getCoinTokenId(), userCoin.getType());
			tokenCoin.setAccountNum(DecimalUtils.add(tokenCoin.getAccountNum(), userRecord.getValue()));
			tokenCoin.setUpdateTime(new Date());
			userCoinMapper.updateByPrimaryKey(tokenCoin);

			// 法币账户+gas值
			UserCoin coin = userCoinMapper.selectByWalletIdForUpdate(userCoin.getWalletId(), userCoin.getCoinTokenId(),
					"0");
			String gas = JSONObject.parseObject(userRecord.getRecordInfo()).getString("gas");
			coin.setAccountNum(DecimalUtils.add(coin.getAccountNum(), gas));
			userCoinMapper.updateByPrimaryKey(coin);
		}
		// 更改交易记录状态为失败
		userRecord.setStatus(Dic4UserRecord.STATUSA.FAIL.getKey());
		userRecord.setUpdateTime(new Date());
		userRecordMapper.updateByPrimaryKey(userRecord);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult coinRecharge(String dealId, String txHash, String toAddress, String value) {
		// 生成记录
		UserCoin userCoin = userCoinMapper.selectByAccountAdd(toAddress);
		UserWallet userWallet = userWalletMapper.selectByPrimaryKey(userCoin.getWalletId());
		String ethValue = DecimalUtils.toEth(value);
		UserRecord record = new UserRecord();
		record.setDealId(dealId);
		record.setTxHash(txHash);
		record.setUserId(userWallet.getUserId());
		record.setUserCoinId(userCoin.getId());
		record.setCoinTokenCode("ETH");
		record.setOneLeaveType(Dic4UserRecord.ONE_LEAVE_TYPE.IN_PUT.getKey());
		record.setTwoLeaveType(Dic4UserRecord.TWO_LEAVE_TYPE.RECHARGE.getKey());
		record.setTypeNote(Dic4UserRecord.TWO_LEAVE_TYPE.RECHARGE.getValue());
		record.setStatus("1");
		record.setValue(ethValue);
		userRecordMapper.insertSelective(record);

		// 账户总额加上value
		UserCoin coin = userCoinMapper.selectByWalletIdForUpdate(userCoin.getWalletId(), userCoin.getCoinTokenId(),
				userCoin.getType());
		coin.setAccountNum(DecimalUtils.add(coin.getAccountNum(), ethValue));
		coin.setUpdateTime(new Date());
		userCoinMapper.updateByPrimaryKey(coin);

		return ResultUtils.createSuccess();
	}

}

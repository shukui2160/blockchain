package com.minute.service.wallet.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.tookit.coin.DecimalUtils;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.wallet.entry.Coin;
import com.minute.service.wallet.entry.SysCoin;
import com.minute.service.wallet.entry.SysRecord;
import com.minute.service.wallet.entry.SysWallet;
import com.minute.service.wallet.entry.UserCoin;
import com.minute.service.wallet.entry.UserWallet;
import com.minute.service.wallet.mapper.CoinMapper;
import com.minute.service.wallet.mapper.SysCoinMapper;
import com.minute.service.wallet.mapper.SysRecordMapper;
import com.minute.service.wallet.mapper.SysWalletMapper;
import com.minute.service.wallet.mapper.UserCoinMapper;
import com.minute.service.wallet.mapper.UserRecordMapper;
import com.minute.service.wallet.mapper.UserWalletMapper;
import com.minute.service.wallet.remote.BlockRemoteService;
import com.minute.service.wallet.remote.SystemRemoteService;
import com.minute.service.wallet.remote.UserRemoteService;
import com.minute.service.wallet.service.SysWalletService;

@Service
@Transactional
public class SysWalletServiceImpl implements SysWalletService {

	@Autowired
	private SysWalletMapper sysWalletMapper;
	@Autowired
	private SysCoinMapper sysCoinMapper;
	@Autowired
	private SystemRemoteService systemRemoteService;
	@Autowired
	private CoinMapper coinMapper;
	@Autowired
	private SysRecordMapper sysRecordMapper;
	@Autowired
	private BlockRemoteService blockRemoteService;
	@Autowired
	private UserWalletMapper userWalletMapper;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private UserCoinMapper userCoinMapper;
	
	
	@Override
	public TlabsResult SysWalletMange() {
		// TODO Auto-generated method stub
		List<SysWallet> sysWalletMange = this.sysWalletMapper.sysWalletMange();
		List<Map<String, Object>> data = new ArrayList<>();
		for (SysWallet sysWallet : sysWalletMange) {
			Map<String, Object> sysWalletMap = new HashMap<>();
			Long sysUserId = sysWallet.getSysUserId();
			Long createSysUserId = sysWallet.getCreateSysUserId();
			TlabsResult sysUserName = this.systemRemoteService.selectNameByid(sysUserId.toString());
			TlabsResult createSysUserName = this.systemRemoteService.selectNameByid(createSysUserId.toString());
			sysWalletMap.put("walletName", sysWallet.getWalletName());
			sysWalletMap.put("sysUserName", sysUserName.getDate());
			sysWalletMap.put("walletMoney", sysWallet.getWalletMoney());
			sysWalletMap.put("createSysUserId", createSysUserName.getDate());
			sysWalletMap.put("createtime", sysWallet.getCreateTime());
			data.add(sysWalletMap);
		}
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult SysCoinMange() {
		// TODO Auto-generated method stub
		List<SysCoin> sysCoinMange = this.sysCoinMapper.sysCoinMange();
		List<Map<String, Object>> data = new ArrayList<>();
		for (SysCoin sysCoin : sysCoinMange) {
			Map<String, Object> coinMap = new HashMap<>();
			Long coinId = sysCoin.getCoinId();
			Long walletId = sysCoin.getWalletId();
			String coinName = this.coinMapper.selectCoinName(coinId);
			String walletName = this.sysWalletMapper.selectWalletById(walletId);
			coinMap.put("coinName", coinName);
			coinMap.put("walletName", walletName);
			coinMap.put("address", sysCoin.getAddress());
			coinMap.put("accountNum", sysCoin.getAccountNum());
			coinMap.put("availableNum", sysCoin.getAccountAvailableNum());
			coinMap.put("lockNum", sysCoin.getAccountLockNum());
			coinMap.put("chainBalce", sysCoin.getChainBlance());
			coinMap.put("hashNum", sysCoin.getTransHashNum());
			data.add(coinMap);
		}
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult selectEthColdAddress(String coinName) {
		// TODO Auto-generated method stub
			List<Map<String, Object>> data = new ArrayList<>();
			String coinMange = this.coinMapper.selectMangeBycoinName(coinName);
			Map<String, Object> coinHash = new HashMap<>();
			coinHash.put("coldAddress", this.sysCoinMapper.selectEthColdAddress());
			coinHash.put("hotAddress", this.sysCoinMapper.selectEthHotAddress());
			//coinHash.put("rechargeMin", coinMange.getRechargeMin());
			coinHash.put("defalutGas", coinMange);
			data.add(coinHash);
		
		return ResultUtils.createSuccess(data);
	}
	@Override
	public TlabsResult mentionMoneyToColdWallet(String from, String to,  String value,
			SysRecord sysRecord) {
		// TODO Auto-generated method stub
		/*EthTemplate ethTemplate = new EthTemplate(Environment.RPC_URL);
		String TxHash = ethTemplate.ethTransfer(from, passwd, to, new BigInteger(value));*/
		Coin selectByPrimaryKey = this.coinMapper.selectByPrimaryKey(sysRecord.getCoinId());
		String gas = DecimalUtils.toWei(selectByPrimaryKey.getDefalutGas());
		TlabsResult ethTransfer = this.blockRemoteService.ethTransfer(from, to, value,gas);
		if(!ethTransfer.isSuccess()) {
			return ethTransfer;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> ethMap = (Map<String, String>) ethTransfer.getDate();
		sysRecord.setFromAddr(from);
		sysRecord.setToAddr(to);
		sysRecord.setMoneyValue(value);
		sysRecord.setTxid(ethMap.get("txHash"));
		//添加交易记录
		int insertRecord = this.sysRecordMapper.insert(sysRecord);
		if (insertRecord > 0) {
			return ResultUtils.createSuccess();
		} else {
			return ResultUtils.createException();
		}
	}
	@Override
	public TlabsResult mentionMoneyToHotWallet(String from, String to, String value,
			SysRecord sysRecord) {
		// TODO Auto-generated method stub
		/*EthTemplate ethTemplate = new EthTemplate(Environment.RPC_URL);
		String TxHash = ethTemplate.ethTransfer(from, passwd, to, new BigInteger(value));*/
		Coin selectByPrimaryKey = this.coinMapper.selectByPrimaryKey(sysRecord.getCoinId());
		String gas = DecimalUtils.toWei(selectByPrimaryKey.getDefalutGas());
		TlabsResult ethTransfer = this.blockRemoteService.ethTransfer(from, to, value,gas);
		if(!ethTransfer.isSuccess()) {
			return ethTransfer;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> ethMap = (Map<String, String>) ethTransfer.getDate();
		sysRecord.setFromAddr(from);
		sysRecord.setToAddr(to);
		sysRecord.setMoneyValue(value);
		sysRecord.setTxid(ethMap.get("txHash"));
		//添加交易记录
		int insertRecord = this.sysRecordMapper.insert(sysRecord);
		if (insertRecord > 0) {
			return ResultUtils.createSuccess();
		} else {
			return ResultUtils.createException();
		}
	}

	@Override
	public TlabsResult selectTxidByStatus() {
		// TODO Auto-generated method stub
		return ResultUtils.createSuccess(this.sysRecordMapper.selectTxidByStatus());
	}

	@Override
	public TlabsResult updateStatusByTxid(String txid) {
		// TODO Auto-generated method stub
		if(this.sysRecordMapper.updateStatusByTxid(txid)>0){
			return ResultUtils.createSuccess();
		}else {
			return ResultUtils.createException();
		}
	}

	@Override
	public TlabsResult userWalletMange() {
		// TODO Auto-generated method stub
		List<UserWallet> userWalletMange = this.userWalletMapper.userWalletMange();
		List<Map<String, String>> data = new ArrayList<>();
		for (UserWallet userWallet : userWalletMange) {
			Long userId = userWallet.getUserId();
			TlabsResult selectEmailById = this.userRemoteService.selectEmailById(String.valueOf(userId));
			String email = (String) selectEmailById.getDate();
			Map<String, String> userMange = new HashMap<>();
			userMange.put("email", email);
			userMange.put("WalletId", userWallet.getId().toString());
			userMange.put("WalletName", userWallet.getWalletName());
			userMange.put("type", userWallet.getType());
			data.add(userMange);
		}
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult userWalletAddrMange(String walletId) {
		// TODO Auto-generated method stub
		List<UserCoin> selectCoinsByWalletId = this.userCoinMapper.selectCoinsByWalletId(Long.valueOf(walletId));
		return ResultUtils.createSuccess(selectCoinsByWalletId);
	}
}

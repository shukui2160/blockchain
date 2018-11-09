package com.minute.application.manage.service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.manage.entry.MentionRecord;
import com.minute.application.manage.mapper.MentionRecordMapper;
import com.minute.application.manage.remote.SysWalletRemoteService;
import com.minute.application.manage.service.MentionMoneyService;


@Service
@Transactional
public class MentionMoneyServiceImpl implements MentionMoneyService {

	@Autowired
	private MentionRecordMapper mentionRecordMapper;
	
	@Autowired
	private SysWalletRemoteService sysWalletRemoteService;
	
	@Override
	public TlabsResult mentionMoneyMange() {
		// TODO Auto-generated method stub
		List<MentionRecord> mentionMoney = this.mentionRecordMapper.mentionMoneyMange();
		List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
		for (MentionRecord mentionMoney2 : mentionMoney) {
			Map<String, Object> mentionMap = new HashMap<String,Object>();
			TlabsResult selectCoin = this.sysWalletRemoteService.selectCoin(String.valueOf(mentionMoney2.getUserId()), mentionMoney2.getCoinTokenId(), mentionMoney2.getType());
			Map<String, Object> selectUserCoinId = (Map<String, Object>) selectCoin.getDate();
			mentionMap.put("userCoinId", selectUserCoinId.get("id"));
			mentionMap.put("id", mentionMoney2.getId());
			mentionMap.put("user_id", mentionMoney2.getUserId());
			mentionMap.put("deal_id", mentionMoney2.getDealId());
			mentionMap.put("status", mentionMoney2.getStatus());
			mentionMap.put("from_addr", mentionMoney2.getFormAddr());
			mentionMap.put("to_addr", mentionMoney2.getToAddr());
			mentionMap.put("money_value", mentionMoney2.getMoneyValue());
			data.add(mentionMap);
		}
		return ResultUtils.createSuccess(data);
	}
	
	@Override
	public TlabsResult mentionThrough(Long id, String status) {
		// TODO Auto-generated method stub
		int m = 0;
		//修改项目状态
		//System.out.println(id);
		m += this.mentionRecordMapper.updateStatusById(status,id);
		MentionRecord selectByPrimaryKey = this.mentionRecordMapper.selectByPrimaryKey(id);
		String dealId = selectByPrimaryKey.getDealId();
		String toAddress = selectByPrimaryKey.getToAddr();
		TlabsResult agreeDrawCoinToken = this.sysWalletRemoteService.agreeDrawCoinToken(dealId, toAddress);
		if(agreeDrawCoinToken.isSuccess() && m >1) {
			return ResultUtils.createSuccess();
		}else {
			return ResultUtils.createException();
	}
	}
	
	@Override
	public TlabsResult mentionReject(Long id,String status) {
		// TODO Auto-generated method stub
		int m = 0;
		//修改项目状态
		m += this.mentionRecordMapper.updateStatusById(status,id);
		MentionRecord selectByPrimaryKey = this.mentionRecordMapper.selectByPrimaryKey(id);
		String dealId = selectByPrimaryKey.getDealId();
		String toAddress = selectByPrimaryKey.getToAddr();
		TlabsResult refuseDrawCoinToken = this.sysWalletRemoteService.refuseDrawCoinToken(dealId, toAddress);
		if(refuseDrawCoinToken.isSuccess() && m >1) {
			return ResultUtils.createSuccess();
		}else {
			return ResultUtils.createException();
	}
}

	
	

}

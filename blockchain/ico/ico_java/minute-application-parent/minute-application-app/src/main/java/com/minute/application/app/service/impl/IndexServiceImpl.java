package com.minute.application.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSONObject;
import com.common.tookit.coin.DecimalUtils;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.github.pagehelper.PageHelper;
import com.minute.application.app.common.Dic4Project;
import com.minute.application.app.common.Dic4Wallet;
import com.minute.application.app.common.ProjectEnum;
import com.minute.application.app.entry.JoinRecord;
import com.minute.application.app.entry.Project;
import com.minute.application.app.entry.ProjectAccount;
import com.minute.application.app.entry.ProjectActive;
import com.minute.application.app.entry.Team;
import com.minute.application.app.mapper.JoinRecordMapper;
import com.minute.application.app.mapper.ProjectAccountMapper;
import com.minute.application.app.mapper.ProjectActiveMapper;
import com.minute.application.app.mapper.ProjectMapper;
import com.minute.application.app.mapper.TeamMapper;
import com.minute.application.app.remote.CmsRemoteService;
import com.minute.application.app.remote.UserRemoteService;
import com.minute.application.app.remote.WalletRemoteService;
import com.minute.application.app.service.IndexService;

import cn.hutool.core.util.RandomUtil;

@Service
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private TeamMapper teamMapper;

	@Autowired
	private ProjectActiveMapper projectActiveMapper;

	@Autowired
	private JoinRecordMapper joinRecordMapper;

	@Autowired
	private CmsRemoteService cmsRemoteService;

	@Autowired
	private WalletRemoteService walletRemoteService;

	@Autowired
	private UserRemoteService userRemoteService;

	@Autowired
	private ProjectAccountMapper projectAccountMapper;
	

	@Override
	public TlabsResult listPic() {
		// 待实现
		List<String> list = new ArrayList<>();
		list.add("9c51d665-5475-4f6e-bbc1-94fc394edaeb.jpg");
		list.add("9c51d665-5475-4f6e-bbc1-94fc394edaeb.jpg");
		list.add("9c51d665-5475-4f6e-bbc1-94fc394edaeb.jpg");
		list.add("9c51d665-5475-4f6e-bbc1-94fc394edaeb.jpg");
		return ResultUtils.createSuccess(list);
	}

	@Override
	public TlabsResult listProject(String index, String userId, String languageId) {
		PageHelper.startPage(Integer.valueOf(index), 10, false);
		Date currentTime = new Date();
		List<Project> list = null;
		if (userId == null) {
			list = projectMapper.indexList(currentTime);
		} else {
			list = projectMapper.indexListByUserId(Integer.valueOf(userId), currentTime);
		}
		List<Map<String, Object>> data = new ArrayList<>();
		if (list.size() == 0) {
			return ResultUtils.createSuccess(data);
		}
		// cms服务查询数据
		StringBuilder ids = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i == 0) {
				ids.append(list.get(i).getId().toString());
			}
			ids.append(",");
			ids.append(list.get(i).getId().toString());
		}
		TlabsResult cmsResult = cmsRemoteService.selectContext(ids.toString(), "1", languageId);
		if (!"200".equals(cmsResult.getCode())) {
			return cmsResult;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> cmsData = (Map<String, String>) cmsResult.getDate();

		// 数据组装
		for (Project project : list) {
			Map<String, Object> map = new HashMap<>();
			Team team = teamMapper.selectByPrimaryKey(project.getTeamId());
			map.put("projectId", project.getId());
			map.put("teamLogo", team.getTeamLogo());
			map.put("projectLogo", project.getProjectLogo());
			map.put("collectNum", project.getCollectNum());
			// 4 ,5 ,10 待开始，进行中，已结束
			map.put("status",
					getStatus(currentTime, project.getStartTime(), project.getEndTime(), project.getStatus()));
			map.put("startTime", project.getStartTime());
			map.put("endTime", project.getEndTime());
			ProjectActive active = projectActiveMapper.selectByProjectId(project.getId());
			map.put("collectedNum", active.getCollectedNum());

			String context = cmsData.get(project.getId().toString());
			JSONObject jsonContext = JSONObject.parseObject(context).getJSONObject("title");
			map.put("name", jsonContext.getString("name"));
			map.put("brief", jsonContext.getString("brief"));
			map.put("currentDate", currentTime);
			data.add(map);
		}
		return ResultUtils.createSuccess(data);
	}

	private String getStatus(Date currentTime, Date startTime, Date endTime, String status) {
		// status 等于 4 或者 10
		if (currentTime.getTime() > startTime.getTime() && endTime.getTime() > currentTime.getTime()) {
			return Dic4Project.STATUS_UNDERWAY.getKey();
		} else {
			return status;
		}
	}

	@Override
	public TlabsResult pageInfo(String userId, String languageId, String projectId) {
		Project project = projectMapper.selectByPrimaryKey(Long.valueOf(projectId));

		// 调用cms 服务
		TlabsResult cmsResult = cmsRemoteService.selectContext(projectId, "1", languageId);
		if (!"200".equals(cmsResult.getCode())) {
			return cmsResult;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> cmsData = (Map<String, String>) cmsResult.getDate();
		String context = cmsData.get(projectId);

		// 调用wallet 服务
		TlabsResult walletResult = walletRemoteService.selectToken(project.getTokenId());
		if (!"200".equals(walletResult.getCode())) {
			return walletResult;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> walletData = (Map<String, Object>) walletResult.getDate();

		ProjectActive active = projectActiveMapper.selectByProjectId(project.getId());
		Team team = teamMapper.selectByPrimaryKey(project.getTeamId());

		// 数据组装
		Map<String, Object> titleData = new HashMap<>();
		titleData.put("projectId", project.getId());
		titleData.put("projectLogo", project.getProjectLogo());
		JSONObject lan = JSONObject.parseObject(context);
		JSONObject title = lan.getJSONObject("title");
		titleData.put("name", title.getString("name"));
		titleData.put("brief", title.getString("brief"));
		titleData.put("collectNum", project.getCollectNum());
		titleData.put("collectedNumm", active.getCollectedNum());// 已经筹集数量

		Map<String, Object> teamData = new HashMap<>();
		teamData.put("teamName", team.getTeamName());
		teamData.put("teamLogo", team.getTeamLogo());
		teamData.put("teamDescribe", team.getTeamDescribe());

		Map<String, Object> icoData = new HashMap<>();
		icoData.put("startTime", project.getStartTime());
		icoData.put("endTime", project.getEndTime());
		icoData.put("tokenCode", walletData.get("tokenCode"));
		icoData.put("coinId", project.getCoinId());// 比特币 还是以太币
		icoData.put("upExchangeTime", project.getUpExchangeTime()); // 上交易所时间
		icoData.put("coinProportion", project.getCoinProportion());// 兑换比例
		icoData.put("minInvest", project.getMinInvest());
		icoData.put("maxInvest", project.getMaxInvest());
		icoData.put("tokenNum", project.getTokenNum());

		Map<String, Object> otherData = new HashMap<>();
		otherData.put("whitePager", project.getWhitePaper());
		otherData.put("website", project.getWebsite());
		otherData.put("telegramGroup", project.getTelegramGroup());

		Map<String, Object> data = new HashMap<>();
		data.put("titleData", titleData);
		data.put("desData", lan.getJSONArray("des"));
		data.put("teamData", teamData);
		data.put("icoData", icoData);
		data.put("otherData", otherData);

		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult joinProjectInfo(String userId, String languageId, String projectId) {
		Project project = projectMapper.selectByPrimaryKey(Long.valueOf(projectId));

		// 调用cms 服务
		TlabsResult cmsResult = cmsRemoteService.selectContext(projectId, "1", languageId);
		if (!"200".equals(cmsResult.getCode())) {
			return cmsResult;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> cmsData = (Map<String, String>) cmsResult.getDate();
		String context = cmsData.get(projectId);
		JSONObject title = JSONObject.parseObject(context).getJSONObject("title");

		// 调用钱包服务-当前用户coin详情
		TlabsResult walletResult = walletRemoteService.selectCoin(userId, project.getCoinId(),
				Dic4Wallet.WalletCoinType.COIN.getKey());
		if (!"200".equals(walletResult.getCode())) {
			return walletResult;
		}
		@SuppressWarnings("unchecked")
		Map<String, String> walletData = (Map<String, String>) walletResult.getDate();

		ProjectAccount projectAccount = projectAccountMapper.selectByProjectId(project.getId());

		Team team = teamMapper.selectByPrimaryKey(project.getTeamId());
		Map<String, Object> data = new HashMap<>();
		data.put("teamLogo", team.getTeamLogo());
		data.put("teamName", team.getTeamName());
		data.put("name", title.getString("name"));
		data.put("brief", title.getString("brief"));
		data.put("coinId", project.getCoinId());
		data.put("minInvest", project.getMinInvest());
		data.put("maxInvest", project.getMaxInvest());
		data.put("coinProportion", project.getCoinProportion());// 兑换比例
		//data.put("accountAvailableNum", walletData.get("accountAvailableNum"));
		data.put("accountNum", walletData.get("accountNum"));
		data.put("payAccountAdd", walletData.get("accountAdd"));
		data.put("projectAccountAdd", projectAccount.getCoinAddress());
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult joinProject(String userId, String languageId, String projectId, String payPassWd,
			String tokenNum) {

		Project project = projectMapper.selectByPrimaryKey(Long.valueOf(projectId));
		String payNum = DecimalUtils.divide(tokenNum, project.getCoinProportion());
		
		// 项目是否已经结束
		long currentTimeMillis = System.currentTimeMillis();
		if (currentTimeMillis > project.getEndTime().getTime()) {
			return ResultUtils.createResult(ProjectEnum.PROJECT_FINISH.getCode(),
					ProjectEnum.PROJECT_FINISH.getMsg(languageId));
		}

		if (currentTimeMillis < project.getStartTime().getTime()) {
			// return 项目还未开始
			return ResultUtils.createResult(ProjectEnum.PROJECT_FINISH.getCode(),
					ProjectEnum.PROJECT_NO_START.getMsg(languageId));
		}

		if (!Dic4Project.STATUS_WAITSTART.getKey().equals(project.getStatus())) {
			// return 项目无法参与
			return ResultUtils.createResult(ProjectEnum.PROJECT_EXCEPTION.getCode(),
					ProjectEnum.PROJECT_EXCEPTION.getMsg(languageId));
		}

		// 众筹是否已经完成 ; 已筹集数量 + payNum > 总筹集数量
		ProjectActive active = projectActiveMapper.selectByProjectIdForUpdate(Long.valueOf(projectId));
		if (DecimalUtils.compareTT(DecimalUtils.add(active.getCollectedNum(), payNum), project.getCollectNum())) {
			// 返回token数量不足，请重新填写token数量
			return ResultUtils.createResult(ProjectEnum.EXCEED_BALANCE.getCode(),
					ProjectEnum.EXCEED_BALANCE.getMsg(languageId));
		}
		
		// 支付密码是否正确
		TlabsResult userResult = userRemoteService.authPayPasswd(userId, payPassWd, languageId);
		if (!userResult.isSuccess()) {
			return userResult;
		}

		// 项目筹集总额 加payNum
		active.setCollectedNum(DecimalUtils.removeZero(DecimalUtils.add(active.getCollectedNum(), payNum)));
		projectActiveMapper.updateByPrimaryKey(active);

		// 构造项目参与记录,生成流水id
		JoinRecord record = new JoinRecord();
		record.setDealId(RandomUtil.randomUUID()); // 流水id
		record.setUserId(Long.valueOf(userId));
		record.setProjectId(Long.valueOf(projectId));
		record.setTokenNum(tokenNum);
		record.setCoinProportion(project.getCoinProportion());
		record.setPayNum(payNum);
		joinRecordMapper.insertSelective(record);

		// 调用wallet服务进行支付 并生成账单
		// 如果支付失败,抛出异常,事物回滚,日志记录人工处理 分布式事物问题
		ProjectAccount account = projectAccountMapper.selectByProjectId(project.getId());
		TlabsResult walletResult = walletRemoteService.pay4project(userId, project.getCoinId(), record.getDealId(),
				payNum,  tokenNum ,languageId, account.getCoinAddress(), project.getTokenId());
		if (!walletResult.isSuccess()) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return walletResult;
		}
		return ResultUtils.createSuccess();
	}

}

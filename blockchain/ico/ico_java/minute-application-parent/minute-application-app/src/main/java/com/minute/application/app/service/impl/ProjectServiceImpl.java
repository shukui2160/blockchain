package com.minute.application.app.service.impl;

import java.text.SimpleDateFormat;
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

import com.minute.application.app.common.Dic4Project;
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
import com.minute.application.app.remote.BlockRemoteService;
import com.minute.application.app.remote.CmsRemoteService;
import com.minute.application.app.remote.WalletRemoteService;
import com.minute.application.app.service.ProjectService;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper ProjectMapper;

	@Autowired
	private TeamMapper teamMapper;

	@Autowired
	private ProjectActiveMapper projectActiveMapper;

	@Autowired
	private CmsRemoteService cmsRemoteService;

	@Autowired
	private WalletRemoteService walletRemoteService;

	@Autowired
	private ProjectAccountMapper projectAccountMapper;

	@Autowired
	private BlockRemoteService blockRemoteService;

	@Autowired
	private JoinRecordMapper joinRecordMapper;

	private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Override
	public TlabsResult createProject(String userId, String languageId, String projectLogo, String context) {
		// 创建项目-生成项目id
		Project project = new Project();
		project.setUserId(Long.valueOf(userId));
		project.setProjectLogo(projectLogo);
		String projectName = JSONObject.parseObject(context).getJSONObject("title").getString("name");
		project.setProjectName(projectName);
		ProjectMapper.insertSelective(project);

		ProjectActive active = new ProjectActive();
		active.setUserId(Long.valueOf(userId));
		active.setProjectId(project.getId());
		projectActiveMapper.insertSelective(active);

		TlabsResult blockResult = blockRemoteService.createProjectAccount();
		if (!blockResult.isSuccess()) {
			return blockResult;
		}
		@SuppressWarnings("unchecked")
		Map<String, Object> blockData = (Map<String, Object>) blockResult.getDate();
		// 插入project_account 信息
		ProjectAccount projectAccount = new ProjectAccount();
		projectAccount.setProjectId(project.getId());
		projectAccount.setCoinAddress(blockData.get("address").toString());
		projectAccountMapper.insertSelective(projectAccount);

		// 调用cms系统,分布式事物问题,暂时搁置
		TlabsResult crmResult = cmsRemoteService.insertContext(Long.valueOf(languageId), project.getId(), "1", context);
		if (!"200".equals(crmResult.getCode())) {
			throw new RuntimeException("context insert error!");
		}

		Map<String, String> data = new HashMap<>();
		data.put("projectId", String.valueOf(project.getId()));
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult updateProject(String userId, String projectId, String languageId, String projectLogo,
			String context) {
		Project project = new Project();
		project.setId(Long.valueOf(projectId));
		project.setUserId(Long.valueOf(userId));
		project.setProjectLogo(projectLogo);
		String projectName = JSONObject.parseObject(context).getJSONObject("title").getString("name");
		project.setProjectName(projectName);
		project.setUpdateTime(new Date());
		ProjectMapper.updateByPrimaryKeySelective(project);
		TlabsResult crmResult = cmsRemoteService.updateContext(Long.valueOf(languageId), project.getId(), "1", context);
		if (!"200".equals(crmResult.getCode())) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return crmResult;
		}
		Map<String, String> data = new HashMap<>();
		data.put("projectId", String.valueOf(project.getId()));
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult createTeam(String userId, String teamName, String teamLogo, String teamDescribe) {
		Team team = new Team();
		team.setUserId(Long.valueOf(userId));
		team.setTeamName(teamName);
		team.setTeamLogo(teamLogo);
		team.setTeamDescribe(teamDescribe);
		teamMapper.insertSelective(team);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult listTeams(String userId) {
		List<Map<String, String>> data = teamMapper.selectByUserId(Long.valueOf(userId));
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult bindTeam(String projectId, String teamId, String userId) {
		Project project = ProjectMapper.selectByPrimaryKey(Long.valueOf(projectId));
		project.setTeamId(Long.valueOf(teamId));
		project.setUpdateTime(new Date());
		ProjectMapper.updateByPrimaryKeySelective(project);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult createToken(String userId, String tokenAdd, String tokenLogo, String circulation,
			String tokenCode, String tokenDes, String decimals) {
		TlabsResult result = walletRemoteService.createToken(userId, tokenAdd, tokenLogo, circulation, tokenCode,
				tokenDes, decimals);
		return result;
	}

	@Override
	public TlabsResult listTokens(String userId) {
		TlabsResult result = walletRemoteService.listTokens(Long.valueOf(userId));
		return result;
	}

	@Override
	public TlabsResult getTokenInfo(String userId, String tokenAdd) {
		// 根据合约地址 获取token相关信息
		return blockRemoteService.getTokenInfo(tokenAdd);
	}

	@Override
	public TlabsResult bindToken(String userId, String projectId, String tokenId) {
		Project project = ProjectMapper.selectByPrimaryKey(Long.valueOf(projectId));
		project.setTokenId(Long.valueOf(tokenId));
		project.setUpdateTime(new Date());
		ProjectMapper.updateByPrimaryKeySelective(project);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult upload(String userId, String operation, String file, String suffix) {
		String fileName = RandomUtil.randomUUID() + suffix;
		String path = "/data/project/" + fileName;
		FileUtil.writeBytes(Base64.decode(file), path);
		Map<String, String> data = new HashMap<>();
		data.put("fileName", fileName);
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult saveIcoInfo(String userId, String projectId, String tokenNum, String starTime, String endTime,
			String coinProportion, String minInvest, String maxInvest, String upExchangeTime, String coinId,
			String collectNum) {
		// 数据校验 tokenNum , 募集总额 , 兑换比例 , 开始时间 , 结束时间
		Project project = ProjectMapper.selectByPrimaryKey(Long.valueOf(projectId));
		project.setTokenNum(tokenNum);
		project.setStartTime(DateUtil.date(Long.valueOf(starTime)));
		project.setEndTime(DateUtil.date(Long.valueOf(endTime)));
		project.setCoinProportion(coinProportion);
		project.setMinInvest(minInvest);
		project.setMaxInvest(maxInvest);
		project.setUpExchangeTime(DateUtil.date(Long.valueOf(upExchangeTime)));
		project.setCoinId(Long.valueOf(coinId));
		project.setCollectNum(collectNum);
		project.setUpdateTime(new Date());
		ProjectMapper.updateByPrimaryKeySelective(project);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult saveOtherInfo(String userId, String projectId, String whitePaper, String webSite,
			String telegramGroup) {
		Project project = ProjectMapper.selectByPrimaryKey(Long.valueOf(projectId));
		project.setWhitePaper(whitePaper);
		project.setWebsite(webSite);
		project.setTelegramGroup(telegramGroup);
		project.setUpdateTime(new Date());
		ProjectMapper.updateByPrimaryKeySelective(project);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult confirmProject(String userId, String projectId) {
		Project project = ProjectMapper.selectByPrimaryKey(Long.valueOf(projectId));
		project.setStatus(Dic4Project.STATUS_PENDING.getKey());
		project.setUpdateTime(new Date());
		ProjectMapper.updateByPrimaryKeySelective(project);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult createProjectByUserId(String userId) throws Exception {
		// TODO Auto-generated method stub
		List<Project> createProjectByUserId = this.ProjectMapper.createProjectByUserId(new Integer(userId));
		List<Map<String, Object>> dataMange = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date currentTime = sdf.parse(sdf.format(new Date()));
		for (Project project : createProjectByUserId) {
			if (project.getStatus().equals("4")) {
				Date startTime = project.getStartTime();
				Date endTime = project.getEndTime();
				Date starttime = sdf.parse(sdf.format(startTime.getTime()));
				Date endtime = sdf.parse(sdf.format(endTime.getTime()));
				if (currentTime.before(starttime)) {
					project.setStatus("4");
				} else if (currentTime.after(starttime) && currentTime.before(endtime)) {
					project.setStatus("5");
				} else if (currentTime.after(endtime)) {
					project.setStatus("9");
				}
			}
			if(project.getCoinId() == null) {
				project.setCoinId(Long.valueOf("2"));
			}
			if(project.getStatus().equals("0")) {
				if(project.getStartTime() == null) {
					project.setStartTime(currentTime);
				}
				if(project.getEndTime() == null) {
					project.setEndTime(currentTime);
				}
				if(project.getCollectNum() == null) {
					project.setCollectNum("0");;
				}
			}
			// 支持的货币
			Long projectId = project.getId();
			ProjectActive selectByProjectId = this.projectActiveMapper.selectByProjectId(projectId);
			Map<String, Object> data = new HashMap<>();
			data.put("projectName", project.getProjectName());
			data.put("collectNum", project.getCollectNum());
			data.put("collectedNum", selectByProjectId.getCollectedNum());
			data.put("createTime", project.getCreateTime());
			data.put("coinId", project.getCoinId().toString());
			data.put("status", project.getStatus());
			data.put("projectLogo", project.getProjectLogo());
			data.put("id", project.getId());
			dataMange.add(data);
		}

		return ResultUtils.createSuccess(dataMange);
	}

	@Override
	public TlabsResult joinProjectByUserId(String userId) {
		// TODO Auto-generated method stuDate date = new Date();
		List<JoinRecord> joinProjectByUserId = this.joinRecordMapper.joinProjectByUserId(Integer.parseInt(userId));
		List<Map<String, Object>> dataMange = new ArrayList<>();
		for (JoinRecord joinRecord : joinProjectByUserId) {
			Long projectId = joinRecord.getProjectId();
			Project selectByPrimaryKey = this.ProjectMapper.selectByPrimaryKey(projectId);
			ProjectActive selectByProjectId = this.projectActiveMapper.selectByProjectId(projectId);
			Map<String, Object> data = new HashMap<>();
			data.put("projectName", selectByPrimaryKey.getProjectName());
			data.put("collectNum", selectByPrimaryKey.getCollectNum());
			data.put("collectedNum", selectByProjectId.getCollectedNum());
			data.put("projectId", joinRecord.getProjectId());
			data.put("projectLogo", selectByPrimaryKey.getProjectLogo());
			data.put("createTime", selectByPrimaryKey.getCreateTime());
			data.put("coin_id", selectByPrimaryKey.getCoinId());
			dataMange.add(data);
		}
		return ResultUtils.createSuccess(dataMange);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TlabsResult selectProjectMangeById(String id) throws Exception {
		// TODO Auto-generated method stub
		// 根据代币id获取获取代币合约地址
		Project selectProjectMangeById = this.ProjectMapper.selectProjectMangeById(Integer.parseInt(id));
		Long tokenId = selectProjectMangeById.getTokenId();
		Long projectId = selectProjectMangeById.getId();
		TlabsResult selectToken = this.walletRemoteService.selectToken(tokenId);
		Map<String, String> tokenData = (Map<String, String>) selectToken.getDate();
		String tokenAdd = tokenData.get("tokenAdd");
		//修改项目状态值
		Project selectByPrimaryKey = this.ProjectMapper.selectByPrimaryKey(projectId);
		//String status = selectByPrimaryKey.getStatus();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date currentTime = sdf.parse(sdf.format(new Date()));
		if (selectByPrimaryKey.getStatus().equals("4")) {
			Date startTime = selectByPrimaryKey.getStartTime();
			Date endTime = selectByPrimaryKey.getEndTime();
			Date starttime = sdf.parse(sdf.format(startTime.getTime()));
			Date endtime = sdf.parse(sdf.format(endTime.getTime()));
			if (currentTime.before(starttime)) {
				selectByPrimaryKey.setStatus("4");
			} else if (currentTime.after(starttime) && currentTime.before(endtime)) {
				selectByPrimaryKey.setStatus("5");
			} else if (currentTime.after(endtime)) {
				selectByPrimaryKey.setStatus("9");
			}
		}
		// 获取项目地址
		ProjectAccount selectByProjectId = this.projectAccountMapper.selectByProjectId(new Long(id));
		String coinAddress = selectByProjectId.getCoinAddress();
		// 获取项目地址中的代币量和eth量
		TlabsResult balance = this.blockRemoteService.getBalance(coinAddress, tokenAdd);
		Map<String, String> coinBalance = (Map<String, String>) balance.getDate();
		String coinNum = coinBalance.get("coinNum");
		// eth转换
		String ethNum = DecimalUtils.toEth(coinNum);
		String tokenNum = coinBalance.get("tokenNum");
		// 获得兑换比例
		String coinProportion = tokenData.get("decimals");
		// 计算token
		log.info("coinProportion" + coinProportion);
		log.info("tokenNum" + tokenNum);
		String tokenNumdata = DecimalUtils.divide(tokenNum, coinProportion);
		// 获取项目简介
		TlabsResult selectContext = this.cmsRemoteService.selectContext(String.valueOf(projectId), "1", "1");
		Map<String, String> cmsData = (Map<String, String>) selectContext.getDate();
		String context = cmsData.get(projectId.toString());
		JSONObject lan = JSONObject.parseObject(context);
		JSONObject title = lan.getJSONObject("title");
		// 组装数据
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("coinNum", ethNum);
		dataMap.put("tokenNum", tokenNumdata);
		dataMap.put("projectName", selectProjectMangeById.getProjectName());
		dataMap.put("projectContext", title.getString("brief"));
		dataMap.put("status", selectByPrimaryKey.getStatus());
		dataMap.put("startTime", selectProjectMangeById.getStartTime());
		dataMap.put("endTime", selectProjectMangeById.getEndTime());
		dataMap.put("currentTime", System.currentTimeMillis());
		dataMap.put("projectLogo", selectProjectMangeById.getProjectLogo());
		return ResultUtils.createSuccess(dataMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TlabsResult makeSureProject(String id) {
		// TODO Auto-generated method stub
		// 回显相关信息
		ProjectAccount selectByProjectId = this.projectAccountMapper.selectByProjectId(new Long(id));
		String coinAddress = selectByProjectId.getCoinAddress();
		Project selectProjectMangeById = this.ProjectMapper.selectProjectMangeById(Integer.parseInt(id));
		Long tokenId = selectProjectMangeById.getTokenId();
		TlabsResult selectToken = this.walletRemoteService.selectToken(tokenId);
		Map<String, String> tokenData = (Map<String, String>) selectToken.getDate();
		String tokenAdd = tokenData.get("tokenAdd");
		// 获取项目地址中的代币量和eth量
		TlabsResult balance = this.blockRemoteService.getBalance(coinAddress, tokenAdd);
		Map<String, String> coinBalance = (Map<String, String>) balance.getDate();
		String coinNum = coinBalance.get("coinNum");
		// eth转换
		String ethNum = DecimalUtils.toEth(coinNum);
		String tokenNum = coinBalance.get("tokenNum");
		// 获得兑换比例
		String coinProportion = tokenData.get("decimals");
		// 计算token
		String tokenNumdata = DecimalUtils.divide(tokenNum, coinProportion);
		// 项目开始时间和结束时间
		Project selectByPrimaryKey = this.ProjectMapper.selectByPrimaryKey(new Long(id));
		Date startTime = selectByPrimaryKey.getStartTime();
		Date endTime = selectByPrimaryKey.getEndTime();
		// 组装数据
		Map<String, Object> dataMap = new HashMap<>();
		dataMap.put("projectId", selectByProjectId.getId());
		dataMap.put("coinNum", ethNum);
		dataMap.put("tokenNum", tokenNumdata);
		dataMap.put("projectAddr", coinAddress);
		dataMap.put("startTime", startTime);
		dataMap.put("endTime", endTime);
		return ResultUtils.createSuccess(dataMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public TlabsResult makeSure(String id, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		// 根据代币id获取获取代币合约地址
		Project selectProjectMangeById = this.ProjectMapper.selectProjectMangeById(Integer.parseInt(id));
		Long tokenId = selectProjectMangeById.getTokenId();
		// Long projectId = selectProjectMangeById.getId();
		TlabsResult selectToken = this.walletRemoteService.selectToken(tokenId);
		Map<String, String> tokenData = (Map<String, String>) selectToken.getDate();
		String tokenAdd = tokenData.get("tokenAdd");
		// 获取项目地址
		ProjectAccount selectByProjectId = this.projectAccountMapper.selectByProjectId(new Long(id));
		String coinAddress = selectByProjectId.getCoinAddress();
		// 获取项目地址中的代币量和eth量
		TlabsResult balance = this.blockRemoteService.getBalance(coinAddress, tokenAdd);
		Map<String, String> coinBalance = (Map<String, String>) balance.getDate();
		String coinNum = coinBalance.get("coinNum");
		String tokenNum = coinBalance.get("tokenNum");
		// 获得总募集数()
		String collectNum = selectProjectMangeById.getCollectNum();
		// 数据库中tokenNum数量
		String tokenNum2 = selectProjectMangeById.getTokenNum();
		// 获得token兑换最小单位时兑换比例
		String coinProportion = tokenData.get("decimals");
		// 计算数据库中token数量最小单位时的数量
		String tokenNumdata = DecimalUtils.divide(tokenNum2, coinProportion);
		// 获得最小募集数
		String minInvest = selectProjectMangeById.getMinInvest();
		// 获得最大募集数
		// String maxInvest = selectProjectMangeById.getMaxInvest();
		// 计算可以最小需要分成多少份募集数
		String minSlice = DecimalUtils.divide(tokenNum2, minInvest);
		double minSliceCeil = Math.ceil(Double.parseDouble(minSlice));
		// 计算可以最多需要分成多少份募集数
		// String maxSlice = DecimalUtils.divide(collectNum, maxInvest);
		// 获得手续费
		Map<String, String> defalutGasData = (Map<String, String>) this.walletRemoteService.defalutGas("eth").getDate();
		String defalutGas = defalutGasData.get("defalutGas");
		// 计算总手续费单位eth
		String numGasEth = DecimalUtils.multiply(String.valueOf(minSliceCeil), defalutGas);
		// 计算总手续费单位为wei
		String coinNumdata = DecimalUtils.toWei(numGasEth);
		// 比较连上和数据库中的token数量和法币数量是否满足上线条件
		if (DecimalUtils.compareTT(tokenNum, tokenNumdata) && DecimalUtils.compareTT(coinNum, coinNumdata)) {
			try {
				int updateStatusById = this.ProjectMapper.makeSure("4", Long.valueOf(id), startTime, endTime);
				if (updateStatusById > 0) {
					return ResultUtils.createSuccess();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ResultUtils.createResult("201", "法币需充值" + numGasEth + "且代币需充值" + tokenNum2 + "充值金额不足请及时充值");
	}

}

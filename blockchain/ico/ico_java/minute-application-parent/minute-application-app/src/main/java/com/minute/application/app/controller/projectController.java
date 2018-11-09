package com.minute.application.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.common.tookit.result.CommonUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.application.app.config.RequestContext;
import com.minute.application.app.config.RequireLogin;
import com.minute.application.app.service.ProjectService;

import cn.hutool.core.util.StrUtil;

//发布项目-流程
@RestController
@RequestMapping("/project")
public class projectController {

	@Autowired
	private ProjectService projectService;

	// 录入项目基本信息，创建项目
	@RequireLogin
	@PostMapping("/createProject")
	public TlabsResult createProject(@RequestBody JSONObject request) {
		String projectLogo = request.getString("projectLogo");
		String context = request.getJSONObject("context").toJSONString();
		String projectId = request.getString("projectId");
		TlabsResult verifyResult = CommonUtils.verifyNull(projectLogo, context);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		String languageId = RequestContext.get().getLanguageId();
        if (StrUtil.isEmpty(projectId)) {
        	return projectService.createProject(userId, languageId, projectLogo, context);
		}else {
			return projectService.updateProject(userId, projectId, languageId, projectLogo, context);
		}
		
	}

	// 用户创建团队
	@RequireLogin
	@PostMapping("/createTeam")
	public TlabsResult createTeam(@RequestBody JSONObject request) {
		String teamName = request.getString("teamName");
		String teamLogo = request.getString("teamLogo");
		String teamDescribe = request.getString("teamDescribe");
		TlabsResult verifyResult = CommonUtils.verifyNull(teamName, teamLogo, teamDescribe);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.createTeam(userId, teamName, teamLogo, teamDescribe);
	}

	// 用户查询自己创建的团队
	@RequireLogin
	@PostMapping("/listTeams")
	public TlabsResult listTeams() {
		String userId = RequestContext.get().getUserId();
		return projectService.listTeams(userId);
	}

	// 当前项目绑定team信息
	@RequireLogin
	@PostMapping("/bindTeam")
	public TlabsResult bindTeam(@RequestBody JSONObject request) {
		String projectId = request.getString("projectId");
		String teamId = request.getString("teamId");
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId, teamId);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.bindTeam(projectId, teamId, userId);
	}

	// 根据合约地址 校验合约地址的有效性，并返回token详情
	// 暂时不用
	@RequireLogin
	@PostMapping("/getTokenInfo")
	public TlabsResult getTokenInfo(@RequestBody JSONObject request) {
		String tokenAdd = request.getString("tokenAdd");
		TlabsResult verifyResult = CommonUtils.verifyNull(tokenAdd);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.getTokenInfo(userId, tokenAdd);
	}

	// 创建代币
	@RequireLogin
	@PostMapping("/createToken")
	public TlabsResult createToken(@RequestBody JSONObject request) {
		String tokenAdd = request.getString("tokenAdd");
		String tokenLogo = request.getString("tokenLogo");
		String circulation = request.getString("circulation");
		String decimals = request.getString("decimals");
		String tokenCode = request.getString("tokenCode");
		String tokenDes = request.getString("tokenDes");
		TlabsResult verifyResult = CommonUtils.verifyNull(tokenAdd, tokenLogo, circulation, decimals, tokenCode);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.createToken(userId, tokenAdd, tokenLogo, circulation, tokenCode, tokenDes, decimals);
	}

	// 根据用户id查看用户创建的代币
	@RequireLogin
	@PostMapping("/listTokens")
	public TlabsResult selectToken() {
		String userId = RequestContext.get().getUserId();
		return projectService.listTokens(userId);
	}

	// 根据用户id查看用户创建的代币
	@RequireLogin
	@PostMapping("/bindToken")
	public TlabsResult bindToken(@RequestBody JSONObject request) {
		String projectId = request.getString("projectId");
		String tokenId = request.getString("tokenId");
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId, tokenId);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.bindToken(userId, projectId, tokenId);
	}

	// 上传文件
	@RequireLogin
	@PostMapping("/upload")
	public TlabsResult upload(@RequestBody JSONObject request) {
		String operation = "project";
		String file = request.getString("file");
		String suffix = request.getString("suffix");
		TlabsResult verifyResult = CommonUtils.verifyNull(file, suffix);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.upload(userId, operation, file, suffix);
	}

	// 保存ICO信息
	@RequireLogin
	@PostMapping("/saveIcoInfo")
	public TlabsResult saveIcoInfo(@RequestBody JSONObject request) {
		String projectId = request.getString("projectId");
		String tokenNum = request.getString("tokenNum");// 本次众筹-对应代币数量
		String starTime = request.getString("starTime");// 众筹-开始时间
		String endTime = request.getString("endTime");// 众筹-结束时间
		String coinProportion = request.getString("coinProportion");// 兑换比例
		String minInvest = request.getString("minInvest");// 最小投资限额 token
		String maxInvest = request.getString("maxInvest");// 最大投资限额 token
		String upExchangeTime = request.getString("upExchangeTime");// 上交易所时间
		String coinId = request.getString("coinId");// 项目所支持的法币类型
		String collectNum = request.getString("collectNum");
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId, tokenNum, starTime, endTime, coinProportion,
				minInvest, maxInvest, upExchangeTime, coinId, collectNum);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.saveIcoInfo(userId, projectId, tokenNum, starTime, endTime, coinProportion, minInvest,
				maxInvest, upExchangeTime, coinId, collectNum);
	}

	// 保存ICO信息
	@RequireLogin
	@PostMapping("/saveOtherInfo")
	public TlabsResult saveOtherInfo(@RequestBody JSONObject request) {
		String projectId = request.getString("projectId");
		String whitePaper = request.getString("whitePaper");// 白皮书地址
		String webSite = request.getString("webSite");// 官方网站
		String telegramGroup = request.getString("telegramGroup");// 电报群
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId, whitePaper);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.saveOtherInfo(userId, projectId, whitePaper, webSite, telegramGroup);
	}

	// 确认提交-项目变为待审批状态
	@RequireLogin
	@PostMapping("/confirmProject")
	public TlabsResult confirmProject(@RequestBody JSONObject request) {
		String projectId = request.getString("projectId");
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId);
		if (verifyResult != null) {
			return verifyResult;
		}
		String userId = RequestContext.get().getUserId();
		return projectService.confirmProject(userId, projectId);
	}

}

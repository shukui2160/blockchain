package com.minute.application.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.minute.application.app.service.IndexService;

//首页 项目列表 ,项目详情页,参加项目
@RestController
@RequestMapping("/index")
public class IndexController {

	@Autowired
	private IndexService indexService;

	private static final Logger log = LoggerFactory.getLogger(IndexController.class);

	// 首页轮播图
	@PostMapping("/listPic")
	public TlabsResult picList() {
		return indexService.listPic();
	}

	// 首页列表-规则参考showDoc
	@PostMapping("/listProject")
	public TlabsResult listProject(@RequestBody JSONObject request) {
		String index = request.getString("index");// 第几页数据
		String userId = RequestContext.get().getUserId();
		String languageId = RequestContext.get().getLanguageId();
		TlabsResult verifyResult = CommonUtils.verifyNull(index);
		if (verifyResult != null) {
			return verifyResult;
		}
		return indexService.listProject(index, userId, languageId);
	}

	// 项目详情页
	@PostMapping("/pageInfo")
	public TlabsResult pageInfo(@RequestBody JSONObject request) {
		String projectId = request.getString("projectId");
		String userId = RequestContext.get().getUserId();
		String languageId = RequestContext.get().getLanguageId();
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId);
		if (verifyResult != null) {
			return verifyResult;
		}
		return indexService.pageInfo(userId, languageId, projectId);
	}

	// 参与项目页 数据回显 需要先登录
	@RequireLogin
	@PostMapping("/joinProjectInfo")
	public TlabsResult joinProjectInfo(@RequestBody JSONObject request) {
		String projectId = request.getString("projectId");
		String userId = RequestContext.get().getUserId();
		String languageId = RequestContext.get().getLanguageId();
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId);
		if (verifyResult != null) {
			return verifyResult;
		}
		return indexService.joinProjectInfo(userId, languageId, projectId);
	}

	// 参与项目信息提交 需要先登录 并设置过支付密码
	@RequireLogin
	@PostMapping("/joinProject")
	public TlabsResult joinProject(@RequestBody JSONObject request) {
		log.info("joinProject:" + request.toJSONString());
		String projectId = request.getString("projectId");
		String payPassWd = request.getString("payPassWd");
		String tokenNum = request.getString("tokenNum"); // token数量
		String userId = RequestContext.get().getUserId();
		String languageId = RequestContext.get().getLanguageId();
		TlabsResult verifyResult = CommonUtils.verifyNull(projectId, payPassWd, tokenNum);
		if (verifyResult != null) {
			return verifyResult;
		}
		return indexService.joinProject(userId, languageId, projectId, payPassWd, tokenNum);
	}

}

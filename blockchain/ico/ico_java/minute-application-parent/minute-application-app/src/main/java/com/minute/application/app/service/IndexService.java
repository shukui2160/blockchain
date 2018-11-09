package com.minute.application.app.service;

import com.common.tookit.result.TlabsResult;

public interface IndexService {

	TlabsResult listPic();

	TlabsResult listProject(String index, String userId, String languageId);

	TlabsResult pageInfo(String userId, String languageId, String projectId);

	TlabsResult joinProjectInfo(String userId, String languageId, String projectId);

	TlabsResult joinProject(String userId, String languageId, String projectId, String payPassWd, String tokenNum);
}

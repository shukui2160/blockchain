package com.minute.application.app.service;





import java.util.Date;

import com.common.tookit.result.TlabsResult;


public interface ProjectService {

	TlabsResult createProject(String userId, String languageId, String projectLogo, String context);
	
	TlabsResult updateProject(String userId, String projectId, String languageId, String projectLogo, String context);

	TlabsResult createTeam(String userId, String teamName, String teamLogo, String teamDescribe);

	TlabsResult listTeams(String userId);

	TlabsResult createToken(String userId, String tokenAdd, String tokenLogo, String circulation, String tokenCode,
			String tokenDes,String decimals);

	TlabsResult listTokens(String userId);

	TlabsResult upload(String userId, String operation, String file, String suffix);

	TlabsResult getTokenInfo(String userId, String tokenAdd);

	TlabsResult bindTeam(String projectId, String teamId, String userId);

	TlabsResult bindToken(String userId, String projectId, String tokenId);

	TlabsResult saveIcoInfo(String userId, String projectId, String tokenNum, String starTime, String endTime,
			String coinProportion, String minInvest, String maxInvest, String upExchangeTime, String coinId,String collectNum);

	TlabsResult saveOtherInfo(String userId, String projectId, String whitePaper, String webSite, String telegramGroup);

	TlabsResult confirmProject(String userId, String projectId);
	
	TlabsResult createProjectByUserId(String userId) throws Exception;
	
    TlabsResult joinProjectByUserId(String userId);
    
    TlabsResult selectProjectMangeById(String id) throws Exception;
    
    TlabsResult makeSureProject(String id);
    
    TlabsResult makeSure(String id,Date startTime,Date endTime);

	
    

}

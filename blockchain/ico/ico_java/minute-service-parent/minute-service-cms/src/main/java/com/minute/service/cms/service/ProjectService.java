package com.minute.service.cms.service;

import com.common.tookit.result.TlabsResult;

public interface ProjectService {

	public TlabsResult insertContext(Long lanId, Long topicId, String topicType, String context);

	public TlabsResult selectContext(String topicIds, String topicType, String lanId);
	
	public TlabsResult contextMange(String topicId);

	public TlabsResult updateContext(Long lanId, Long topicId, String topicType, String context);

}

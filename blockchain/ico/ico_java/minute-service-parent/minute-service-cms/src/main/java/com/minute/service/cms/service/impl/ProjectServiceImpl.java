package com.minute.service.cms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.common.tookit.result.ResultUtils;
import com.common.tookit.result.TlabsResult;
import com.minute.service.cms.entry.LanContext;
import com.minute.service.cms.mapper.LanContextMapper;
import com.minute.service.cms.service.ProjectService;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private LanContextMapper lanContextMapper;

	@Override
	public TlabsResult insertContext(Long lanId, Long topicId, String topicType, String context) {
		LanContext record = new LanContext();
		record.setTopicId(topicId);
		record.setTopicType(topicType);
		record.setLanId(lanId);
		record.setContext(context);
		lanContextMapper.insertSelective(record);
		return ResultUtils.createSuccess();
	}

	@Override
	public TlabsResult selectContext(String topicIds, String topicType, String lanId) {
		Map<String, String> data = new HashMap<>();
		String[] topicIdArray = topicIds.split(",");
		for (String topicId : topicIdArray) {
			String context = lanContextMapper.selectContext(Long.valueOf(topicId), topicType, Long.valueOf(lanId));
			if (context == null) {
				String otherLonId = (lanId.equals("1") ? "2" : "1");
				context = lanContextMapper.selectContext(Long.valueOf(topicId), topicType, Long.valueOf(otherLonId));
			}
			data.put(topicId, context);
		}
		return ResultUtils.createSuccess(data);
	}

	@Override
	public TlabsResult contextMange(String topicId) {
		return ResultUtils.createSuccess(this.lanContextMapper.contextMange(Long.valueOf(topicId)));
	}

	@Override
	public TlabsResult updateContext(Long lanId, Long topicId, String topicType, String context) {
		LanContext record = lanContextMapper.selectByTopicIdAndTopicType(Long.valueOf(topicId), topicType,
				Long.valueOf(lanId));
		record.setContext(context);
		record.setUpdateTime(new Date());
		lanContextMapper.updateByPrimaryKeySelective(record);
		return ResultUtils.createSuccess();
	}

}

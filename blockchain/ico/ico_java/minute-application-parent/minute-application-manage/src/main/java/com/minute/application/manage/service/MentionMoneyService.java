package com.minute.application.manage.service;

import com.common.tookit.result.TlabsResult;


public interface MentionMoneyService {
	 //select mentionMoney mange
    TlabsResult mentionMoneyMange();
    
    //through
    TlabsResult mentionThrough(Long id,String status);
    
    //reject
    TlabsResult mentionReject(Long id,String status);
}

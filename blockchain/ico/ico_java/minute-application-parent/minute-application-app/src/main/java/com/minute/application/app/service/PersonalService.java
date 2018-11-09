package com.minute.application.app.service;

import com.common.tookit.result.TlabsResult;

public interface PersonalService {

	TlabsResult index(String userId);

	TlabsResult listCoin(String userId);

	TlabsResult listBills(Long userId, Long userCoinId, String index);

	TlabsResult bileInfo(String userId, String userRecordId);

	TlabsResult ethGasValue(String coinId);

	TlabsResult drawCoinToken(String payPassWd, String userCoinId, String userId, String coinTokenId, String type,
			String toAddress, String value, String languageId);

}

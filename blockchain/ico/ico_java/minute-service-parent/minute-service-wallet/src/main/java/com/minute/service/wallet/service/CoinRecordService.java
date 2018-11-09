package com.minute.service.wallet.service;

import com.common.tookit.result.TlabsResult;

public interface CoinRecordService {

	TlabsResult listBills(Long userId, Long userCoinId, String index);

	TlabsResult bileInfo(String userId, String userRecordId);

	TlabsResult drawCoinToken(String userId, String dealId, String userCoinId, String toAddress, String value,
			String languageId);

	TlabsResult getCoinInfo(String coinId);

	TlabsResult agreeDrawCoinToken(String dealId, String toAddress);

	TlabsResult refuseDrawCoinToken(String dealId, String toAddress);

	TlabsResult coinRecharge(String dealId, String txHash, String toAddress, String value);

}

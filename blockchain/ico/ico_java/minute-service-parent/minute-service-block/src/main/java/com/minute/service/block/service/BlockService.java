package com.minute.service.block.service;

import com.common.tookit.result.TlabsResult;

public interface BlockService {

	TlabsResult getBlockList();

	void synBlock(String num);

	TlabsResult initBlock(Long fromNum, Long toNum);

	TlabsResult notifyWallet();

}

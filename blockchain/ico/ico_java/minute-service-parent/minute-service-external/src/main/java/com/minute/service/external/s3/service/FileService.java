package com.minute.service.external.s3.service;

import com.minute.service.external.s3.service.impl.FileSO;

import java.io.InputStream;
import java.util.Map;


public interface FileService {

    /**
     *
     * @param publicAccess
     * @param fileInStream
     * @param contentLength
     * @param contentType
     * @param userMetaData
     * @return
     */
    public String uploadFile(
            Boolean publicAccess,
            InputStream fileInStream,
            Long contentLength,
            String contentType,
            Map<String, String> userMetaData);

    /**
     *
     * @param fileUrl
     * @return
     */
    public FileSO getFile(String fileUrl);
}

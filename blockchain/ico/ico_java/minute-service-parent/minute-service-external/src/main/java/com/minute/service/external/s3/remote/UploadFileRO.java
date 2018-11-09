package com.minute.service.external.s3.remote;

import java.io.InputStream;
import java.util.Map;

public class UploadFileRO {
    private String fileKey;
    private Boolean publicAccess;
    private InputStream fileInStream;
    private Long fileContentLength;
    private String fileContentType;
    private Map<String, String> userMetadata;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Boolean getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(Boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public InputStream getFileInStream() {
        return fileInStream;
    }

    public void setFileInStream(InputStream fileInStream) {
        this.fileInStream = fileInStream;
    }

    public Long getFileContentLength() {
        return fileContentLength;
    }

    public void setFileContentLength(Long fileContentLength) {
        this.fileContentLength = fileContentLength;
    }

    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata = userMetadata;
    }
}

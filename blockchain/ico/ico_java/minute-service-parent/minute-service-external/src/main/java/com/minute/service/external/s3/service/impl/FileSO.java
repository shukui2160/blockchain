package com.minute.service.external.s3.service.impl;

import java.io.InputStream;

public class FileSO {
    private String originalName;
    private String contentType;
    private InputStream fileInStream;

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getFileInStream() {
        return fileInStream;
    }

    public void setFileInStream(InputStream fileInStream) {
        this.fileInStream = fileInStream;
    }
}

package com.minute.service.external.s3.controller;


import com.common.tookit.result.TlabsResult;

public class UploadFileResult extends TlabsResult {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

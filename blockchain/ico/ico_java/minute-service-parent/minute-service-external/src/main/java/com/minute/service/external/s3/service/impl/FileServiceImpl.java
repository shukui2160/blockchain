package com.minute.service.external.s3.service.impl;

import com.common.tookit.date.DateTimeUtils;
import com.minute.service.external.common.FileNameUtils;
import com.minute.service.external.s3.remote.AmazonS3Client;
import com.minute.service.external.s3.remote.GetFileRO;
import com.minute.service.external.s3.remote.UploadFileRO;
import com.minute.service.external.s3.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

@Service
@PropertySource("classpath:/conf/amazon.properties")
public class FileServiceImpl implements FileService {

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    public String uploadFile(
            Boolean publicAccess,
            InputStream fileInStream,
            Long contentLength,
            String contentType,
            Map<String, String> userMetaData) {

        StringBuilder fileKeyBuilder = new StringBuilder();

        if (publicAccess) {
            fileKeyBuilder.append("public");
        } else {
            fileKeyBuilder.append("private");
        }

        String month = DateTimeUtils.formatYYYYMMDD(new Date());
        fileKeyBuilder.append("/").append(month).append("/");

        String fileOriginalName = userMetaData.get("file-original-name");
        String fileName = generateFileName(fileOriginalName);
        fileKeyBuilder.append(fileName);

        String fileUrl = "";
        try {
            UploadFileRO fileRO = new UploadFileRO();
            fileRO.setFileKey(fileKeyBuilder.toString());
            fileRO.setFileContentLength(contentLength);
            fileRO.setFileContentType(contentType);
            fileRO.setFileInStream(fileInStream);
            fileRO.setPublicAccess(publicAccess);
            fileRO.setUserMetadata(userMetaData);

            fileUrl = amazonS3Client.uploadFile(fileRO);
        } catch (Exception e) {
            //TODO log the exception and throw user define exception
            e.printStackTrace();
        }

        return fileUrl;
    }

    private String generateFileName(String fileOriginalName) {
        String fileNamePrefix = FileNameUtils.generateUniqueFileName();

        int dotPosition = fileOriginalName.lastIndexOf(".");
        if (dotPosition <= 0) {
            return fileNamePrefix;
        } else {
            String fileNameSuffix = fileOriginalName.substring(dotPosition, fileOriginalName.length());

            return fileNamePrefix + fileNameSuffix;
        }

    }

    public FileSO getFile(String fileUrl) {
        //obtain fileKey according to fileUrl
        int bucketNamePosition = fileUrl.lastIndexOf(bucketName);
        int fileKeyStartPosition = bucketNamePosition + bucketName.length() + 1;
        int fileKeyEndPosition = fileUrl.length();
        String fileKey = fileUrl.substring(fileKeyStartPosition, fileKeyEndPosition);

        GetFileRO getFileRO = amazonS3Client.obtainFile(fileKey);

        FileSO fileSO = new FileSO();
        fileSO.setContentType(getFileRO.getContentType());
        fileSO.setOriginalName(getFileRO.getOriginalName());
        fileSO.setFileInStream(getFileRO.getFileInStream());

        return fileSO;
    }

}

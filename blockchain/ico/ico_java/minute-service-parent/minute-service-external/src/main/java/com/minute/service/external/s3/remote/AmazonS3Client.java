package com.minute.service.external.s3.remote;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.minute.service.external.common.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@PropertySource("classpath:/conf/amazon.properties")
public class AmazonS3Client {

    private AmazonS3 amazonS3;

    @Value("${amazon.s3.region}")
    private String region;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    public AmazonS3Client (AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     *
     * @return 文件 URL
     * @throws Exception
     */
    public String uploadFile(UploadFileRO fileRO) throws Exception {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(fileRO.getUserMetadata());
        objectMetadata.setContentLength(fileRO.getFileContentLength());
        objectMetadata.setContentType(fileRO.getFileContentType());

        PutObjectRequest request = new PutObjectRequest(bucketName, fileRO.getFileKey(), fileRO.getFileInStream(), objectMetadata);

        if (fileRO.getPublicAccess()) {
            request.setCannedAcl(CannedAccessControlList.PublicRead);
        } else {
            request.setCannedAcl(CannedAccessControlList.Private);
        }

        //TODO request.setSdkRequestTimeout(); timeout unit?

        PutObjectResult putObjectResult = amazonS3.putObject(request);

        StringBuilder builder = new StringBuilder();
        String hostName = bucketName; //DNS域名映射，bucket name 与 映射的二级域名需要一致
        builder.append("http://")
                .append(hostName)
                .append("/")
                .append(fileRO.getFileKey());

        return builder.toString();
    }

    /**
     *
     * @param fileKey 文件Key 名称
     * @return
     */
    public GetFileRO obtainFile(String fileKey) {

        GetFileRO getFileRO = new GetFileRO();

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileKey);

        //TODO request.setSdkRequestTimeout(); timeout unit ?
        S3Object objectResult = amazonS3.getObject(getObjectRequest);

        //TODO key maybe not exists, amazon will return an exception (Status Code: 404; Error Code: NoSuchKey; AmazonS3Exception)
        ObjectMetadata objectMetadata = objectResult.getObjectMetadata();
        String contentType = objectMetadata.getContentType();
        getFileRO.setContentType(contentType);

        Map<String, String> userMetaData = objectMetadata.getUserMetadata();
        String originalFileName = userMetaData.get(Constant.FILE_ORIGINAL_NAME);
        getFileRO.setOriginalName(originalFileName);

        getFileRO.setFileInStream(objectResult.getObjectContent());

        return getFileRO;
    }

}

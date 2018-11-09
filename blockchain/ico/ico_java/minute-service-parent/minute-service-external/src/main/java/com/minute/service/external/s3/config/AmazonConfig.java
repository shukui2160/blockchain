package com.minute.service.external.s3.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.minute.service.external.s3.remote.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/conf/amazon.properties")
public class AmazonConfig {

    @Value("${amazon.s3.region}")
    private String region;

    @Value("${amazon.s3.bucket.name}")
    private String bucketName;

    @Bean
    public AmazonS3Client amazonS3Client() {
        return new AmazonS3Client(amazonS3());
    }

    @Bean
    public AmazonS3 amazonS3() {
        //client configuration
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTP);
        clientConfiguration.setSignerOverride("S3SignerType");

        //credential provider
        String credentialProperties = "/conf/amazon.properties";
        AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider(credentialProperties);

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withClientConfiguration(clientConfiguration)
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();

        /**
         * https://docs.aws.amazon.com/zh_cn/sdk-for-java/v1/developer-guide/java-dg-jvm-ttl.html
         */
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");

        return s3Client;
    }

}

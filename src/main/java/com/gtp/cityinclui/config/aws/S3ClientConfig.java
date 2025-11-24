package com.gtp.cityinclui.config.aws;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class S3ClientConfig {

    @Bean
    public S3AsyncClient s3AsyncClient(
            @Value("${aws.s3.region}") String region,
            @Value("${aws.s3.access-key}") String accessKey,
            @Value("${aws.s3.secret-key}") String secretKey)
    {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey,secretKey);
        return S3AsyncClient.builder().region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
    }
}

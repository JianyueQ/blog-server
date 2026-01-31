package com.blog.framework.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio 对象存储配置类
 *
 * @author 31373
 */
@Configuration
public class MinioConfig {

    /**
     * MinIO服务地址
     */
    @Value("${minio.endpoint}")
    private String endpoint;

    /**
     * 存储桶名称
     */
    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 访问KEY
     */
    @Value("${minio.accessKey}")
    private String accessKey;

    /**
     * 私有KEY
     */
    @Value("${minio.secretKey}")
    private String secretKey;

    /**
     * 获取MinIO客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}

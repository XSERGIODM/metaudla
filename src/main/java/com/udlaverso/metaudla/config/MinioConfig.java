package com.udlaverso.metaudla.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.secure}")
    private boolean secure;

    @Bean
    public MinioClient minioClient() {
        String finalEndpoint = secure ? endpoint.replace("http://", "https://") : endpoint;
        return MinioClient.builder()
                .endpoint(finalEndpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
package com.cont.spring.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shining
 */
@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig {
    /**
     * minio部署的机器ip地址
     */
    private String url;
    /**
     *唯一标识的账户
     */
    private String accessKey;

    /**
     * 账户的密码
     */
    private String secretKey;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(url)
				.credentials(accessKey, secretKey).build();
    }
    
}
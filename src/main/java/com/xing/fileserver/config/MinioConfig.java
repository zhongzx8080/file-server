package com.xing.fileserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioConfig {

    private String endpoint;

    private String user;

    private String password;

    private String bucket;

    private int expire;
}

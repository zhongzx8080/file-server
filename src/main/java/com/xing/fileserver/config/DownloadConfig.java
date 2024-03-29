package com.xing.fileserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "download")
@Configuration
@Data
public class DownloadConfig {

    private String host;

    private String api;

    private boolean presigned;
}

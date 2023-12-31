package com.redislabs.edu.redisearch.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis")
@Data
public class RedisProperties {

    private String host;
    private Integer port;
    private String password;
}

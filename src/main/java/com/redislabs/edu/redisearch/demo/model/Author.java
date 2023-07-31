package com.redislabs.edu.redisearch.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@RedisHash
public class Author {

    @Id
    private String id;
    private String name;
}

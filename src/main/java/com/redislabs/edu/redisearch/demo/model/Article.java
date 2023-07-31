package com.redislabs.edu.redisearch.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

import java.util.Set;

@Data
@AllArgsConstructor
@RedisHash
public class Article {

    @Id
    private String id;
    private String title;
    private Double price;

    @Reference
    private Set<Author> authors;

    public void addAuthor(Author author) {
        authors.add(author);
    }
}

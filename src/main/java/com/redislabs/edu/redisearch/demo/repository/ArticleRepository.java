package com.redislabs.edu.redisearch.demo.repository;

import com.redislabs.edu.redisearch.demo.model.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, String> {}
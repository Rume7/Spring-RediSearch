package com.redislabs.edu.redisearch.demo.repository;

import com.redislabs.edu.redisearch.demo.model.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {}

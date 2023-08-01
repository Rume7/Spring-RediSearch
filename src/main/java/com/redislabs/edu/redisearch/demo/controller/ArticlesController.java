package com.redislabs.edu.redisearch.demo.controller;

import com.redislabs.edu.redisearch.demo.config.JedisConfiguration;
import io.redisearch.Query;
import io.redisearch.SearchResult;
import io.redisearch.Suggestion;
import io.redisearch.client.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticlesController {

    private final JedisConfiguration jedisCfg;

    @GetMapping("/search")
    SearchResult search(@RequestParam(name = "q") String query,
                         @RequestParam(defaultValue = "-1") Double minPrice,
                         @RequestParam(defaultValue = "-1") Double maxPrice) {

        try (var client = new Client("article-idx",
                jedisCfg.jedisConnectionFactory().getHostName(), jedisCfg.jedisConnectionFactory().getPort())) {
            var q  = new Query(query);
            if (minPrice != -1 && maxPrice != -1) {
                q.addFilter(new Query.NumericFilter("price", minPrice, maxPrice));
            }
            q.returnFields("title", "price");
            return client.search(q);
        }
    }

    @GetMapping("/authors")
    Collection<Suggestion> authorAutoComplete(@RequestParam(name = "q") String query) {
        return null;
    }
}

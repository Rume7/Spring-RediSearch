package com.redislabs.edu.redisearch.demo;

import com.github.javafaker.Faker;
import com.redislabs.edu.redisearch.demo.model.Article;
import com.redislabs.edu.redisearch.demo.repository.ArticleRepository;
import com.redislabs.edu.redisearch.demo.repository.AuthorRepository;
import io.redisearch.client.Client;
import io.redisearch.Schema;
import io.redisearch.client.IndexDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.*;
import java.util.stream.IntStream;

import static com.redislabs.edu.redisearch.demo.config.JedisConfiguration.getFakeArticleTitle;

@SpringBootApplication
public class RediSearchApplication {

    @Bean
    CommandLineRunner makeMeSomeData(ArticleRepository articleRepo, //
                                     AuthorRepository authorRepo, //
                                     RedisTemplate<String, String> redisTemplate, //
                                     JedisConnectionFactory cf //
    ) {
        return args -> {
            if (articleRepo.count() == 0) {
                var faker = new Faker();

                IntStream.range(0, 250).forEach(n -> {
                    var author = new com.redislabs.edu.redisearch.demo.model.Author(null, faker.name().fullName());
                    authorRepo.save(author);
                });

                var random = new Random();
                var titles = new HashSet<String>();
                IntStream.range(0, 2500).forEach(n -> {
                    var articleTitle = getFakeArticleTitle(faker);
                    while (titles.contains(articleTitle)) {
                        articleTitle = getFakeArticleTitle(faker);
                    }
                    titles.add(articleTitle);

                    var article = new Article(null, articleTitle, Double.parseDouble(faker.commerce().price()), new HashSet<>());
                    IntStream.range(0, random.nextInt(2) + 1).forEach(j -> {
                        var authorId = redisTemplate.opsForSet().randomMember(com.redislabs.edu.redisearch.demo.model.Author.class.getName());
                        article.addAuthor(new com.redislabs.edu.redisearch.demo.model.Author(authorId, null));
                    });
                    articleRepo.save(article);
                });
            }
        };
    }

    @Bean
    CommandLineRunner createSearchIndices(JedisConnectionFactory cf) {
        return args -> {
            var searchIndexName = "article-idx";

            try (var client = new Client(searchIndexName, cf.getHostName(), cf.getPort())) {
                var sc = new Schema()
                        .addSortableTextField("title", 1.0)
                        .addSortableNumericField("price");

                var def = new IndexDefinition().setPrefixes(String.format("%s:", Article.class.getName()));
                client.createIndex(sc, Client.IndexOptions.defaultOptions().setDefinition(def));
            } catch (JedisDataException jde) {

            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(RediSearchApplication.class, args);
    }
}
package com.redislabs.edu.redisearch.demo.config;

import com.github.javafaker.Faker;
import com.redislabs.edu.redisearch.demo.model.ArticleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

import java.util.Map;

@Configuration
public class JedisConfiguration {

    @Autowired
    RedisProperties redisProperties;

    public JedisConfiguration(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
                this.redisProperties.getHost(), this.redisProperties.getPort());
        return new JedisConnectionFactory(config);
    }

    @Bean
    RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    static Map<ArticleType, String> titles = Map.of(
            ArticleType.INFLUENCE, "The influence of %s on %s", //
            ArticleType.LEARN, "What I learn %s from %s", //
            ArticleType.COOK, "How to cook %s with %s", //
            ArticleType.TEACH, "Teach your %s how to pilot a %s " //
    );

    public static String getFakeArticleTitle(Faker faker) {
        var keys = titles.keySet();
        var rando = (long) (keys.size() * Math.random());
        var key = keys.stream().skip(rando).findAny().get();
        var param1 = "";
        var param2 = "";
        switch (key) {
            case INFLUENCE:
                param1 = faker.ancient().god();
                param2 = faker.food().dish();
                break;
            case LEARN:
                param1 = faker.artist().name();
                param2 = faker.backToTheFuture().character();
                break;
            case COOK:
                param1 = faker.food().dish();
                param2 = StringUtils.capitalize(faker.animal().name());
                break;
            case TEACH:
                param1 = faker.animal().name();
                param2 = faker.aviation().aircraft();
                break;
        }
        return String.format(titles.get(key), param1, param2);
    }
}

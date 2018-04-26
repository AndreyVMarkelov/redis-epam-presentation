package ru.andreymarkelov.test.redisdemo.cron;

import io.lettuce.core.RedisClient;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.lettuce.core.RedisURI.builder;

@Configuration
public class RedisConfig {

    @Bean
    public RedisClient redisClient(RedisProperties redisProperties) {
        return RedisClient.create(
                builder().withHost(redisProperties.getHost()).withPort(redisProperties.getPort()).build()
        );
    }
}

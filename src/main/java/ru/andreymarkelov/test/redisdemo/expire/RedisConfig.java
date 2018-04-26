package ru.andreymarkelov.test.redisdemo.expire;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
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

    @Bean
    public StatefulRedisPubSubConnection<String, String> redisPubSubConnection(RedisClient redisClient) {
        return redisClient.connectPubSub();
    }
}

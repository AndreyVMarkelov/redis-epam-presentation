package ru.andreymarkelov.test.redisdemo.expire;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.andreymarkelov.test.redisdemo.expire")
public class ExpireApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ExpireApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ExpireApplication.class, args);
    }

    private final StatefulRedisPubSubConnection<String, String> redisPubSubConnection;

    public ExpireApplication(StatefulRedisPubSubConnection<String, String> redisPubSubConnection) {
        this.redisPubSubConnection = redisPubSubConnection;
    }

    @Override
    public void run(String... args) {
        redisPubSubConnection.sync().configSet("notify-keyspace-events", "Ex");
        redisPubSubConnection.addListener(new RedisPubSubAdapter<String, String>() {
            @Override
            public void message(String channel, String message) {
                log.info("channel:{}, message:{}", channel, message);
            }

            @Override
            public void subscribed(String channel, long count) {
                log.info("subscribed to:{}", channel);
            }
        });
        redisPubSubConnection.sync().subscribe("__keyevent@0__:expired");
    }
}

package ru.andreymarkelov.test.redisdemo.pubsub;

import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.andreymarkelov.test.redisdemo.pubsub")
public class PubSubApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(PubSubApplication.class, args).close();
    }

    private final StatefulRedisConnection<String, String> redisConnection;

    public PubSubApplication(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 100; i++) {
            Thread.sleep(1000);
            redisConnection.sync().publish("pubsub", "" + i);
        }
    }
}

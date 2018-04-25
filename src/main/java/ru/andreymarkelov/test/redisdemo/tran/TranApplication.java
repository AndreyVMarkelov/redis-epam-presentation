package ru.andreymarkelov.test.redisdemo.tran;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.andreymarkelov.test.redisdemo.tran")
public class TranApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(TranApplication.class, args).close();
    }

    private final StatefulRedisConnection<String, String> redisConnection;

    public TranApplication(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection;
    }

    @Override
    public void run(String... args) throws Exception {
        RedisCommands<String, String> sync = redisConnection.sync();
        sync.multi();
        for (int i = 0; i < 100000; i++) {
            sync.set("tranKey" + i, "" + i);
        }
        sync.exec();
    }
}

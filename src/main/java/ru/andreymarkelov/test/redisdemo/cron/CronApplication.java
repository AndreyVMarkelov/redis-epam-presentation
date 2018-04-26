package ru.andreymarkelov.test.redisdemo.cron;

import java.time.Instant;

import com.github.davidmarquis.redisscheduler.RedisTaskScheduler;
import com.github.davidmarquis.redisscheduler.TaskTriggerListener;
import com.github.davidmarquis.redisscheduler.drivers.lettuce.LettuceDriver;
import io.lettuce.core.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.andreymarkelov.test.redisdemo.cron")
public class CronApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(CronApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CronApplication.class, args);
    }

    private final RedisClient redisClient;

    public CronApplication(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public void run(String... args) {
        String name = String.valueOf(System.currentTimeMillis());

        RedisTaskScheduler scheduler = new RedisTaskScheduler(new LettuceDriver(redisClient), new TaskTriggerListener() {
            @Override
            public void taskTriggered(String s) {
                log.info(s + ":" + name);
            }
        });
        scheduler.setPollingDelayMillis(1000);
        scheduler.scheduleAt("taskId", Instant.now().plusSeconds(5));
        scheduler.start();
    }
}

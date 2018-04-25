package ru.andreymarkelov.test.redisdemo.delayqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.andreymarkelov.test.redisdemo.delayqueue")
public class DelayQueueApplication {
    public static void main(String[] args) {
        SpringApplication.run(DelayQueueApplication.class, args);
    }
}

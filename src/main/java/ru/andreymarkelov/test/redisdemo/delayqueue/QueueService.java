package ru.andreymarkelov.test.redisdemo.delayqueue;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.lettuce.core.Range;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class QueueService {
    private static final Logger log = LoggerFactory.getLogger(QueueService.class);

    private static final String QUEUE_NAME = "delay";

    private final StatefulRedisConnection<String, String> redisConnection;
    private final ScheduledExecutorService executorService;

    public QueueService(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection;
        this.executorService = Executors.newScheduledThreadPool(2);
        this.executorService.scheduleAtFixedRate(
                () -> takeMessages(),
                0,
                1,
                TimeUnit.SECONDS
        );
    }

    public void queueMessage(String message, int delay) {
        long time = MILLISECONDS.toSeconds(System.currentTimeMillis()) + delay;
        redisConnection.sync().zadd(QUEUE_NAME, time, message);
    }

    public void takeMessages() {
        Range range = Range.create(0, MILLISECONDS.toSeconds(System.currentTimeMillis()));

        RedisCommands<String, String> sync = redisConnection.sync();
        sync.multi();
        sync.zrevrangebyscore(QUEUE_NAME, range);
        sync.zremrangebyscore(QUEUE_NAME, range);
        TransactionResult transactionResult = sync.exec();

        List<String> messages = transactionResult.get(0);
        messages.forEach(x -> log.info("Processed message:{}", x));
    }
}

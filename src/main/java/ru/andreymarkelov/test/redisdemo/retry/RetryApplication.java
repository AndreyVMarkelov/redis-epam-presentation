package ru.andreymarkelov.test.redisdemo.retry;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.KeyValue;
import io.lettuce.core.api.StatefulRedisConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ru.andreymarkelov.test.redisdemo.retry")
public class RetryApplication implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(RetryApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RetryApplication.class, args).close();
    }

    private final StatefulRedisConnection<String, String> redisConnection;
    private final ObjectMapper objectMapper;

    public RetryApplication(StatefulRedisConnection<String, String> redisConnection) {
        this.redisConnection = redisConnection;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run(String... args) throws Exception {
        Data data = new Data("sample", 0);
        redisConnection.sync().lpush("retry", objectMapper.writeValueAsString(data));

        boolean b = true;
        while (b) {
            KeyValue<String, String> keyValue = redisConnection.sync().blpop(1, "retry");
            if (keyValue != null) {
                Data parsedData = objectMapper.readValue(keyValue.getValue(), Data.class);
                try {
                    brokenState(parsedData);
                    b = false;
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    parsedData.count++;
                    redisConnection.sync().lpush("retry", objectMapper.writeValueAsString(parsedData));
                }
            }
        }
    }

    private void brokenState(Data data) {
        if (data.count < 10) {
            throw new RuntimeException("I cannot, sorry. My attempt is " + data.count);
        }
        log.info("I am done!");
    }

    private static class Data {
        String data;
        int count;

        public Data() {
        }

        public Data(String data, int count) {
            this.data = data;
            this.count = count;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "data='" + data + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}

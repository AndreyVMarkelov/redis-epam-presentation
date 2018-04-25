package ru.andreymarkelov.test.redisdemo.lua;

import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootApplication(scanBasePackages = "ru.andreymarkelov.test.redisdemo.lua")
public class LuaApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(LuaApplication.class, args).close();
    }

    private static final String REDIS_LUA_USE_EVAL = "NOSCRIPT No matching script. Please use EVAL.";

    private final ConfigurableApplicationContext context;
    private final StatefulRedisConnection<String, String> redisConnection;

    public LuaApplication(ConfigurableApplicationContext context, StatefulRedisConnection<String, String> redisConnection) {
        this.context = context;
        this.redisConnection = redisConnection;
    }

    @Override
    public void run(String... args) throws Exception {
        RedisScript redisScript = new RedisScript(IOUtils.toString(context.getResource("classpath:lua/countset.lua").getInputStream(), UTF_8));

        Object d = evalScript(redisScript, ScriptOutputType.STATUS, new String[] {"10000"});
d.getClass();
    }

    private Object evalScript(
            RedisScript redisScript,
            ScriptOutputType outputType,
            String[] keys) {
        try {
            return redisConnection.sync().evalsha(redisScript.getSha1(), outputType, keys);
        } catch (Throwable th) {
            if (REDIS_LUA_USE_EVAL.equals(th.getMessage())) {
                return redisConnection.sync().eval(redisScript.getText(), outputType, keys);
            }
            return null;
        }
    }
}

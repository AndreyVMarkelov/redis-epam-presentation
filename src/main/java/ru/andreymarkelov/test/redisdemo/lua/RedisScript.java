package ru.andreymarkelov.test.redisdemo.lua;

import org.springframework.lang.NonNull;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;

public class RedisScript {
    private String text;
    private String sha1;

    public RedisScript(@NonNull String text) {
        this.text = text;
        this.sha1 = sha1Hex(text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
}

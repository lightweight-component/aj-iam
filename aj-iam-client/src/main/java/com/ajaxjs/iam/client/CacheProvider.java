package com.ajaxjs.iam.client;

public interface CacheProvider {
    void save(String key, String value, int expireSeconds);

    String get(String key);

    default <T> T get(String key, Class<T> clazz) {
        throw new NullPointerException();
    }

    void remove(String key);
}

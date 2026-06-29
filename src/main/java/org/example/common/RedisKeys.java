package org.example.common;
public class RedisKeys {

    private RedisKeys() {}

    public static String stockKey(Long goodsId) {
        return "stock:" + goodsId;
    }

    public static String orderKey(Long userId, Long goodsId) {
        return "order:" + userId + ":" + goodsId;
    }

    public static String tokenKey(String token) {
        return "token:" + token;
    }
}
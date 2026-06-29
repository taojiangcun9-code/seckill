package org.example.service;
import org.example.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenService {

    @Autowired
    private RedisService redisService;

    private static final String TOKEN_PREFIX = "token:";

    // 创建 token
    public String createToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");

        redisService.set(
                TOKEN_PREFIX + token,
                userId,
                3600 * 24
        );

        return token;
    }

    // 获取 userId
    public Long getUserId(String token) {
        if (token == null) return null;

        String val = redisService.get(TOKEN_PREFIX + token);
        return val == null ? null : Long.valueOf(val);
    }

    // 删除 token（登出用）
    public void removeToken(String token) {
        redisService.delete(TOKEN_PREFIX + token);
    }
}
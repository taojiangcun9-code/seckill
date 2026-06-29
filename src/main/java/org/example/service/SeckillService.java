package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class SeckillService {

    @Autowired
    private StringRedisTemplate redisTemplate;


    private static final String LUA_SCRIPT =
            "local stock = tonumber(redis.call('get', KEYS[1]))\n" +
                    "\n" +
                    "if not stock then\n" +
                    "    return -2\n" +
                    "end\n" +
                    "\n" +
                    "if stock <= 0 then\n" +
                    "    return -1\n" +
                    "end\n" +
                    "\n" +
                    "if stock - 1 < 0 then\n" +
                    "    return -1\n" +
                    "end\n" +
                    "\n" +
                    "return redis.call('decr', KEYS[1])";


    public Long luaDeductStock(Long goodsId) {

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_SCRIPT);
        script.setResultType(Long.class);

        return redisTemplate.execute(
                script,
                Collections.singletonList("stock:" + goodsId)
        );
    }
}
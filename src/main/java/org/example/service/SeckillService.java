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

    // Lua脚本（原子扣库存）
    private static final String LUA_SCRIPT =
            "local stock = tonumber(redis.call('get', KEYS[1])) " +
                    "if stock == nil then return -2 end " +
                    "if stock <= 0 then return -1 end " +
                    "stock = stock - tonumber(ARGV[1]) " +
                    "redis.call('set', KEYS[1], stock) " +
                    "return stock";

    /**
     * Lua原子扣库存
     * @param goodsId 商品ID
     * @return
     *  -2：商品不存在
     *  -1：库存不足
     *  >=0：剩余库存
     */
    public Long luaDeductStock(Long goodsId) {

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptText(LUA_SCRIPT);
        script.setResultType(Long.class);

        return redisTemplate.execute(
                script,
                Collections.singletonList("stock:" + goodsId),
                "1"
        );
    }
}
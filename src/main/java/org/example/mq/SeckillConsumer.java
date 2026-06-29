package org.example.mq;

import org.example.mapper.OrderMapper;
import org.example.service.RedisService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SeckillConsumer {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RabbitListener(queues = "seckill.queue")
    public void consume(SeckillMessage msg) {

        Long userId = msg.getUserId();
        Long goodsId = msg.getGoodsId();

        String key = "order:" + userId + ":" + goodsId;

        // 幂等：先抢锁
        Boolean ok = redisService.setIfAbsent(key, "1");

        if (Boolean.FALSE.equals(ok)) {
            return; // 已消费过
        }

        try {
            // 落库
            orderMapper.insertOrder(userId, goodsId);

            // 标记成功
            redisService.set(key, "ok");

        } catch (Exception e) {
            // 失败补偿
            redisService.delete(key);
            throw e;
        }
    }
}
package org.example.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.example.mq.SeckillMessage;
import org.example.mapper.OrderMapper;
import org.example.service.RedisService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SeckillConsumer {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 秒杀消息消费（手动ACK + 幂等 + 防重复消费）
     */
    @RabbitListener(queues = "seckill.queue", ackMode = "MANUAL")
    public void consume(Message message, Channel channel) throws Exception {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            // 1. 解析消息
            String json = new String(message.getBody(), "UTF-8");
            SeckillMessage msg = objectMapper.readValue(json, SeckillMessage.class);

            if (msg == null) {
                channel.basicAck(deliveryTag, false);
                return;
            }

            Long userId = msg.getUserId();
            Long goodsId = msg.getGoodsId();

            // 2. 幂等Key（防重复消费）
            String key = "order:" + userId + ":" + goodsId;

            // 3. 幂等判断（抢锁）
            Boolean ok = redisService.setIfAbsent(key, "1");

            if (Boolean.FALSE.equals(ok)) {
                // 已经消费过，直接确认
                channel.basicAck(deliveryTag, false);
                return;
            }

            // 4. 落库（创建订单）
            orderMapper.insertOrder(userId, goodsId);

            // 5. 标记成功
            redisService.set(key, "ok");

            // 6. 手动ACK
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {

            // 7. 失败重试（重新入队）
            channel.basicNack(deliveryTag, false, true);
        }
    }
}
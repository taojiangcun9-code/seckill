package org.example.mq;
import org.example.mq.SeckillMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeckillProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(SeckillMessage msg) {
        rabbitTemplate.convertAndSend(
                "seckill.exchange",
                "seckill.routing",
                msg
        );
    }
}
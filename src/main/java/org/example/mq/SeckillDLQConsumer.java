package org.example.mq;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class SeckillDLQConsumer {

    @RabbitListener(queues = "seckill.dlq.queue")
    public void handle(String msg) {
        System.out.println("死信消息：" + msg);
        // 后续：人工补偿 / 日志 / 告警
    }
}
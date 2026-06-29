package org.example.config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "seckill.queue";
    public static final String EXCHANGE = "seckill.exchange";
    public static final String ROUTING_KEY = "seckill.routing";

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue dlqQueue() {
        return new Queue("seckill.dlq.queue", true);
    }
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange("seckill.dlx");
    }
    @Bean
    public Queue queue() {
        return QueueBuilder.durable("seckill.queue")
                .deadLetterExchange("seckill.dlx")
                .deadLetterRoutingKey("seckill.dlq")
                .build();
    }
    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(dlqQueue())
                .to(dlxExchange())
                .with("seckill.dlq");
    }
}
package com.tuum.cbs.services;

import com.tuum.cbs.beans.common.response.Response;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqMessageService implements MessageServiceI {
    private RabbitTemplate rabbitTemplate;

    public RabbitMqMessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;
    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    public void send(Response responseMessage) {
        rabbitTemplate.convertAndSend(exchange, routingKey, responseMessage);
    }
}

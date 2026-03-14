package com.pragma.restaurants.infrastructure.out.event.adapter;

import com.pragma.restaurants.domain.model.events.OrderReadyEvent;
import com.pragma.restaurants.domain.spi.IOrderEventPort;
import com.pragma.restaurants.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class RabbitMQOrderEventAdapter implements IOrderEventPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishOrderReady(OrderReadyEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_READY_ROUTING_KEY,
                event
        );
    }
}
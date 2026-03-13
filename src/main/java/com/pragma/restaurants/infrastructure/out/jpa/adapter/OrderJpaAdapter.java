package com.pragma.restaurants.infrastructure.out.jpa.adapter;

import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.spi.IOrderPersistencePort;
import com.pragma.restaurants.infrastructure.mapper.IOrderEntityMapper;
import com.pragma.restaurants.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.restaurants.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final OrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    @Override
    public Order save(Order order) {
        OrderEntity entity = orderEntityMapper.toEntity(order);
        entity.getDishes().forEach(dish -> dish.setOrder(entity));
        return orderEntityMapper.toDomain(orderRepository.save(entity));
    }

    @Override
    public boolean hasActiveOrder(Long clientId) {
        return orderRepository.existsActiveOrderByClientId(clientId);
    }
}

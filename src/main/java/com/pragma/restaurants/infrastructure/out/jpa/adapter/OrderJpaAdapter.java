package com.pragma.restaurants.infrastructure.out.jpa.adapter;

import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.enums.OrderStatus;
import com.pragma.restaurants.domain.spi.IOrderPersistencePort;
import com.pragma.restaurants.infrastructure.mapper.IOrderEntityMapper;
import com.pragma.restaurants.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.restaurants.infrastructure.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Page<Order> findByRestaurantAndStatus(Long restaurantId, OrderStatus status,
                                                 int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<OrderEntity> springPage =
                orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable);

        List<Order> orders = springPage.getContent()
                .stream()
                .map(orderEntityMapper::toDomain)
                .collect(Collectors.toList());

        return new Page<>(
                orders,
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.isLast()
        );
    }
}

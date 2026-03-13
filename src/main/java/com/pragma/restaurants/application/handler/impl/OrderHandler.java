package com.pragma.restaurants.application.handler.impl;

import com.pragma.restaurants.application.dto.OrderRequest;
import com.pragma.restaurants.application.dto.OrderResponse;
import com.pragma.restaurants.application.handler.IOrderHandler;
import com.pragma.restaurants.application.mapper.IOrderRequestMapper;
import com.pragma.restaurants.application.mapper.IOrderResponseMapper;
import com.pragma.restaurants.domain.api.IOrderServicePort;
import com.pragma.restaurants.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = orderRequestMapper.toDomain(orderRequest);
        return orderResponseMapper.toResponse(orderServicePort.save(order));
    }
}

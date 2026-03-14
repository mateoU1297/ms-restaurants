package com.pragma.restaurants.application.handler.impl;

import com.pragma.restaurants.application.dto.DeliverOrderRequest;
import com.pragma.restaurants.application.dto.OrderRequest;
import com.pragma.restaurants.application.dto.OrderResponse;
import com.pragma.restaurants.application.dto.PagedOrderResponse;
import com.pragma.restaurants.application.handler.IOrderHandler;
import com.pragma.restaurants.application.mapper.IOrderPageMapper;
import com.pragma.restaurants.application.mapper.IOrderRequestMapper;
import com.pragma.restaurants.application.mapper.IOrderResponseMapper;
import com.pragma.restaurants.domain.api.IOrderServicePort;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.enums.OrderStatus;
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
    private final IOrderPageMapper orderPageMapper;

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = orderRequestMapper.toDomain(orderRequest);
        return orderResponseMapper.toResponse(orderServicePort.save(order));
    }

    @Override
    public PagedOrderResponse listOrdersByStatus(String status, int page, int size) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        Page<Order> orderPage = orderServicePort.findByRestaurantAndStatus(orderStatus, page, size);
        return orderPageMapper.toResponse(orderPage);
    }

    @Override
    public OrderResponse assignEmployeeToOrder(Long orderId) {
        return orderResponseMapper.toResponse(orderServicePort.assignEmployee(orderId));
    }

    @Override
    public OrderResponse notifyOrderReady(Long orderId) {
        return orderResponseMapper.toResponse(orderServicePort.notifyOrderReady(orderId));
    }

    @Override
    public OrderResponse deliverOrder(Long orderId, DeliverOrderRequest deliverOrderRequest) {
        return orderResponseMapper.toResponse(
                orderServicePort.deliverOrder(orderId, deliverOrderRequest.getSecurityPin())
        );
    }

}

package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.enums.OrderStatus;

public interface IOrderServicePort {

    Order save(Order order);

    Page<Order> findByRestaurantAndStatus(OrderStatus status, int page, int size);

    Order assignEmployee(Long orderId);

    Order notifyOrderReady(Long orderId);
}

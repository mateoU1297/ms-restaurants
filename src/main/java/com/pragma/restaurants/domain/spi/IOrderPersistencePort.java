package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.enums.OrderStatus;

public interface IOrderPersistencePort {

    Order save(Order order);

    boolean hasActiveOrder(Long clientId);

    Page<Order> findByRestaurantAndStatus(Long restaurantId, OrderStatus status, int page, int size);

    Order findById(Long orderId);
}

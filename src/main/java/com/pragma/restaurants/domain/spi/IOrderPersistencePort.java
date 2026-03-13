package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.Order;

public interface IOrderPersistencePort {

    Order save(Order order);

    boolean hasActiveOrder(Long clientId);
}

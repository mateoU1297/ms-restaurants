package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.Order;

public interface IOrderServicePort {

    Order save(Order order);
}

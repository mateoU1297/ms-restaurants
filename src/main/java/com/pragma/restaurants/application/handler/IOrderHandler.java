package com.pragma.restaurants.application.handler;

import com.pragma.restaurants.application.dto.OrderRequest;
import com.pragma.restaurants.application.dto.OrderResponse;

public interface IOrderHandler {

    OrderResponse createOrder(OrderRequest orderRequest);
}

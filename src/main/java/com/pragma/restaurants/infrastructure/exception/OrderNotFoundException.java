package com.pragma.restaurants.infrastructure.exception;

public class OrderNotFoundException extends InfrastructureException {

    public OrderNotFoundException(Long id) {
        super("ORDER_NOT_FOUND", String.format("Order with id %d not found", id));
    }
}

package com.pragma.restaurants.domain.exception;

public class OrderNotReadyException extends DomainException {

    public OrderNotReadyException(String message) {
        super("ORDER_NOT_READY", message);
    }
}

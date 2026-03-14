package com.pragma.restaurants.domain.exception;

public class OrderCannotBeCancelledException extends DomainException {

    public OrderCannotBeCancelledException(String message) {
        super("ORDER_CANNOT_BE_CANCELLED", message);
    }
}
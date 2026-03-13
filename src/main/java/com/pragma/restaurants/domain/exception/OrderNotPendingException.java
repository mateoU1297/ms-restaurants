package com.pragma.restaurants.domain.exception;

public class OrderNotPendingException extends DomainException {

    public OrderNotPendingException(String message) {
        super("ORDER_NOT_PENDING", message);
    }
}
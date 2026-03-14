package com.pragma.restaurants.domain.exception;

public class OrderNotInPreparationException extends DomainException {

    public OrderNotInPreparationException(String message) {
        super("ORDER_NOT_IN_PREPARATION", message);
    }
}
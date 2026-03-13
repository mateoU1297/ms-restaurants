package com.pragma.restaurants.domain.exception;

public class OrderNotFromRestaurantException extends DomainException {

    public OrderNotFromRestaurantException(String message) {
        super("ORDER_NOT_FROM_RESTAURANT", message);
    }
}

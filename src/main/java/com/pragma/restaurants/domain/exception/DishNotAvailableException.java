package com.pragma.restaurants.domain.exception;

public class DishNotAvailableException extends DomainException {

    public DishNotAvailableException(String message) {
        super("DISH_NOT_AVAILABLE", message);
    }
}
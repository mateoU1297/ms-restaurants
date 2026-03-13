package com.pragma.restaurants.domain.exception;

public class DishNotBelongsToRestaurantException extends DomainException {

    public DishNotBelongsToRestaurantException(String message) {
        super("DISH_NOT_BELONGS_TO_RESTAURANT", message);
    }
}

package com.pragma.restaurants.infrastructure.exception;

public class DishNotFoundException extends InfrastructureException {
    
    public DishNotFoundException(Long id) {
        super("DISH_NOT_FOUND", String.format("Dish with id %d not found", id));
    }
}

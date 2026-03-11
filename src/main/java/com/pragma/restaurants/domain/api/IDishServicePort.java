package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.Dish;

public interface IDishServicePort {

    Dish save(Dish dish);

    Dish update(Long dishId, Dish dish);

    Dish toggleActive(Long dishId);
}

package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Page;

public interface IDishServicePort {

    Dish save(Dish dish);

    Dish update(Long dishId, Dish dish);

    Dish toggleActive(Long dishId);

    Page<Dish> findByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}

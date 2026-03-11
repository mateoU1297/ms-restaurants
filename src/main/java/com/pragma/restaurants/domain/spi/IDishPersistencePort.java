package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Page;

public interface IDishPersistencePort {

    Dish save(Dish dish);

    Dish findById(Long id);

    Dish update(Dish dish);

    Page<Dish> findByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}

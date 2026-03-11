package com.pragma.restaurants.application.handler;

import com.pragma.restaurants.application.dto.DishRequest;
import com.pragma.restaurants.application.dto.DishResponse;
import com.pragma.restaurants.application.dto.DishUpdateRequest;

public interface IDishHandler {

    DishResponse createDish(DishRequest dishRequest);

    DishResponse updateDish(Long dishId, DishUpdateRequest dishUpdateRequest);

    DishResponse toggleDishActive(Long dishId);
}

package com.pragma.restaurants.application.handler;

import com.pragma.restaurants.application.dto.DishRequest;
import com.pragma.restaurants.application.dto.DishResponse;

public interface IDishHandler {

    DishResponse createDish(DishRequest dishRequest);
}

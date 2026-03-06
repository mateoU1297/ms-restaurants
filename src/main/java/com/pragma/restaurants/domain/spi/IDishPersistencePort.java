package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.Dish;

public interface IDishPersistencePort {

    Dish save(Dish dish);
}

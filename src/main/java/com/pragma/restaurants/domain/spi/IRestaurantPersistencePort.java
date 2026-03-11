package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;

public interface IRestaurantPersistencePort {

    Restaurant save(Restaurant restaurant);

    Restaurant findById(Long id);

    Page<Restaurant> findAll(int page, int size);
}

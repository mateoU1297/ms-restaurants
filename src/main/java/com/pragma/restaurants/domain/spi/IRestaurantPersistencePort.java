package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.Restaurant;

public interface IRestaurantPersistencePort {

    Restaurant save(Restaurant restaurant);

    Restaurant findById(Long id);
}

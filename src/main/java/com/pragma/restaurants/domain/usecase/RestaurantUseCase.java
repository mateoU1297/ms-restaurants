package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IRestaurantServicePort;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantPersistencePort.save(restaurant);
    }
}

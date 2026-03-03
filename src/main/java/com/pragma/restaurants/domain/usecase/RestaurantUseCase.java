package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IRestaurantServicePort;
import com.pragma.restaurants.domain.model.Restaurant;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantServicePort restaurantServicePort;

    public RestaurantUseCase(IRestaurantServicePort restaurantServicePort) {
        this.restaurantServicePort = restaurantServicePort;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantServicePort.save(restaurant);
    }
}

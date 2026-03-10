package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.RestaurantEmployee;

public interface IRestaurantEmployeePersistencePort {

    RestaurantEmployee save(RestaurantEmployee restaurantEmployee);
}

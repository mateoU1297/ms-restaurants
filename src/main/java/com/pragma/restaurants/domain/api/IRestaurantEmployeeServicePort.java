package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.RestaurantEmployee;

public interface IRestaurantEmployeeServicePort {

    RestaurantEmployee save(RestaurantEmployee restaurantEmployee);
}

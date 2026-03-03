package com.pragma.restaurants.application.handler;

import com.pragma.restaurants.application.dto.RestaurantResponse;
import com.pragma.restaurants.domain.model.Restaurant;

public interface IRestaurantHandler {

    RestaurantResponse saveRestaurant(Restaurant restaurant);
}

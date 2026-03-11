package com.pragma.restaurants.application.handler;

import com.pragma.restaurants.application.dto.PagedRestaurantResponse;
import com.pragma.restaurants.application.dto.RestaurantRequest;
import com.pragma.restaurants.application.dto.RestaurantResponse;

public interface IRestaurantHandler {

    RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest);

    PagedRestaurantResponse listRestaurants(int page, int size);
}

package com.pragma.restaurants.application.handler;

import com.pragma.restaurants.application.dto.RestaurantEmployeeRequest;
import com.pragma.restaurants.application.dto.RestaurantEmployeeResponse;

public interface IRestaurantEmployeeHandler {

    RestaurantEmployeeResponse createRestaurantEmployee(RestaurantEmployeeRequest request);
}

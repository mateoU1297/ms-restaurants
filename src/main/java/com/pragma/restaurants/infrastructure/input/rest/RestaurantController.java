package com.pragma.restaurants.infrastructure.input.rest;

import com.pragma.restaurants.application.dto.RestaurantEmployeeRequest;
import com.pragma.restaurants.application.dto.RestaurantEmployeeResponse;
import com.pragma.restaurants.application.dto.RestaurantRequest;
import com.pragma.restaurants.application.dto.RestaurantResponse;
import com.pragma.restaurants.application.handler.IRestaurantEmployeeHandler;
import com.pragma.restaurants.application.handler.IRestaurantHandler;
import com.pragma.restaurants.infrastructure.adapter.in.rest.api.RestaurantsApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestaurantController implements RestaurantsApi {

    private final IRestaurantHandler restaurantHandler;
    private final IRestaurantEmployeeHandler restaurantEmployeeHandler;

    @Override
    public ResponseEntity<RestaurantResponse> createRestaurant(RestaurantRequest restaurantRequest) {
        RestaurantResponse restaurantResponse = restaurantHandler.createRestaurant(restaurantRequest);
        return ResponseEntity.ok(restaurantResponse);
    }

    @Override
    public ResponseEntity<RestaurantEmployeeResponse> createRestaurantEmployee(
            RestaurantEmployeeRequest restaurantEmployeeRequest) {
        RestaurantEmployeeResponse restaurantEmployeeResponse = restaurantEmployeeHandler
                .createRestaurantEmployee(restaurantEmployeeRequest);
        return ResponseEntity.ok(restaurantEmployeeResponse);
    }
}

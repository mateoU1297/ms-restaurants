package com.pragma.restaurants.application.handler.impl;

import com.pragma.restaurants.application.dto.RestaurantRequest;
import com.pragma.restaurants.application.dto.RestaurantResponse;
import com.pragma.restaurants.application.handler.IRestaurantHandler;
import com.pragma.restaurants.application.mapper.IRestaurantRequestMapper;
import com.pragma.restaurants.application.mapper.IRestaurantResponseMapper;
import com.pragma.restaurants.domain.api.IRestaurantServicePort;
import com.pragma.restaurants.domain.api.IUserServicePort;
import com.pragma.restaurants.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantHandler implements IRestaurantHandler {

    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;

    private final IUserServicePort userServicePort;
    private final IRestaurantServicePort restaurantServicePort;

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        if (userServicePort.isAdmin(restaurantRequest.getOwnerId())) {
            Restaurant response = restaurantServicePort.save(restaurantRequestMapper.toDomain(restaurantRequest));
            return restaurantResponseMapper.toResponse(response);
        }
        return null;
    }
}

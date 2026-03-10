package com.pragma.restaurants.application.handler.impl;

import com.pragma.restaurants.application.dto.RestaurantEmployeeRequest;
import com.pragma.restaurants.application.dto.RestaurantEmployeeResponse;
import com.pragma.restaurants.application.handler.IRestaurantEmployeeHandler;
import com.pragma.restaurants.application.mapper.IRestaurantEmployeeRequestMapper;
import com.pragma.restaurants.application.mapper.IRestaurantEmployeeResponseMapper;
import com.pragma.restaurants.domain.api.IRestaurantEmployeeServicePort;
import com.pragma.restaurants.domain.model.RestaurantEmployee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantEmployeeHandler implements IRestaurantEmployeeHandler {

    private final IRestaurantEmployeeResponseMapper restaurantEmployeeResponseMapper;
    private final IRestaurantEmployeeRequestMapper restaurantEmployeeRequestMapper;
    private final IRestaurantEmployeeServicePort restaurantEmployeeServicePort;

    @Override
    public RestaurantEmployeeResponse createRestaurantEmployee(RestaurantEmployeeRequest request) {
        RestaurantEmployee model = restaurantEmployeeRequestMapper.toDomain(request);
        return restaurantEmployeeResponseMapper.toResponse(restaurantEmployeeServicePort.save(model));
    }
}

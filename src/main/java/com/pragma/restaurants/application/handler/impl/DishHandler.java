package com.pragma.restaurants.application.handler.impl;

import com.pragma.restaurants.application.dto.DishRequest;
import com.pragma.restaurants.application.dto.DishResponse;
import com.pragma.restaurants.application.dto.DishUpdateRequest;
import com.pragma.restaurants.application.handler.IDishHandler;
import com.pragma.restaurants.application.mapper.IDishRequestMapper;
import com.pragma.restaurants.application.mapper.IDishResponseMapper;
import com.pragma.restaurants.application.mapper.IDishUpdateRequestMapper;
import com.pragma.restaurants.domain.api.IDishServicePort;
import com.pragma.restaurants.domain.model.Dish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IDishUpdateRequestMapper dishUpdateRequestMapper;

    @Override
    public DishResponse createDish(DishRequest dishRequest) {
        Dish dish = dishRequestMapper.toDomain(dishRequest);
        return dishResponseMapper.toResponse(dishServicePort.save(dish));
    }

    @Override
    public DishResponse updateDish(Long dishId, DishUpdateRequest dishUpdateRequest) {
        Dish dish = dishUpdateRequestMapper.toDomain(dishUpdateRequest);
        return dishResponseMapper.toResponse(dishServicePort.update(dishId, dish));
    }

    @Override
    public DishResponse toggleDishActive(Long dishId) {
        return dishResponseMapper.toResponse(dishServicePort.toggleActive(dishId));
    }
}

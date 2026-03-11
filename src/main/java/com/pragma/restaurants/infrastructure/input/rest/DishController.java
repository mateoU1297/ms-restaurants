package com.pragma.restaurants.infrastructure.input.rest;

import com.pragma.restaurants.application.dto.DishRequest;
import com.pragma.restaurants.application.dto.DishResponse;
import com.pragma.restaurants.application.dto.DishUpdateRequest;
import com.pragma.restaurants.application.handler.IDishHandler;
import com.pragma.restaurants.infrastructure.adapter.in.rest.api.DishesApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DishController implements DishesApi {

    private final IDishHandler dishHandler;

    @Override
    public ResponseEntity<DishResponse> createDish(DishRequest dishRequest) {
        DishResponse dishResponse = dishHandler.createDish(dishRequest);
        return ResponseEntity.ok(dishResponse);
    }

    @Override
    public ResponseEntity<DishResponse> updateDish(Long dishId, DishUpdateRequest dishUpdateRequest) {
        DishResponse dishResponse = dishHandler.updateDish(dishId, dishUpdateRequest);
        return ResponseEntity.ok(dishResponse);
    }

    @Override
    public ResponseEntity<DishResponse> toggleDishActive(Long dishId) {
        DishResponse dishResponse = dishHandler.toggleDishActive(dishId);
        return ResponseEntity.ok(dishResponse);
    }
}

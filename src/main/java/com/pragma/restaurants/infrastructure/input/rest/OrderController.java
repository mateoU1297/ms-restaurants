package com.pragma.restaurants.infrastructure.input.rest;

import com.pragma.restaurants.application.dto.OrderRequest;
import com.pragma.restaurants.application.dto.OrderResponse;
import com.pragma.restaurants.application.handler.IOrderHandler;
import com.pragma.restaurants.infrastructure.adapter.in.rest.api.OrdersApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrdersApi {

    private final IOrderHandler orderHandler;

    @Override
    public ResponseEntity<OrderResponse> createOrder(OrderRequest orderRequest) {
        OrderResponse orderResponse = orderHandler.createOrder(orderRequest);
        return ResponseEntity.ok(orderResponse);
    }

}

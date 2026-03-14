package com.pragma.restaurants.application.handler;

import com.pragma.restaurants.application.dto.DeliverOrderRequest;
import com.pragma.restaurants.application.dto.OrderRequest;
import com.pragma.restaurants.application.dto.OrderResponse;
import com.pragma.restaurants.application.dto.PagedOrderResponse;

public interface IOrderHandler {

    OrderResponse createOrder(OrderRequest orderRequest);

    PagedOrderResponse listOrdersByStatus(String status, int page, int size);

    OrderResponse assignEmployeeToOrder(Long orderId);

    OrderResponse notifyOrderReady(Long orderId);

    OrderResponse deliverOrder(Long orderId, DeliverOrderRequest deliverOrderRequest);
}

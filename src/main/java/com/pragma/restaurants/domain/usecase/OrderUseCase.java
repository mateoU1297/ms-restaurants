package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IOrderServicePort;
import com.pragma.restaurants.domain.exception.ClientHasActiveOrderException;
import com.pragma.restaurants.domain.exception.DishNotAvailableException;
import com.pragma.restaurants.domain.exception.DishNotBelongsToRestaurantException;
import com.pragma.restaurants.domain.exception.OrderNotFromRestaurantException;
import com.pragma.restaurants.domain.exception.OrderNotPendingException;
import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.enums.OrderStatus;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IOrderPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final ISecurityContextPort securityContextPort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        ISecurityContextPort securityContextPort,
                        IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.securityContextPort = securityContextPort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
    }

    @Override
    public Order save(Order order) {
        Long clientId = securityContextPort.getAuthenticatedUserId();

        if (orderPersistencePort.hasActiveOrder(clientId))
            throw new ClientHasActiveOrderException(
                    String.format("Client %d already has an active order", clientId)
            );

        restaurantPersistencePort.findById(order.getRestaurantId());

        validateDishes(order);

        order.setClientId(clientId);
        order.setStatus(OrderStatus.PENDING);

        return orderPersistencePort.save(order);
    }

    @Override
    public Page<Order> findByRestaurantAndStatus(OrderStatus status, int page, int size) {
        Long employeeId = securityContextPort.getAuthenticatedUserId();
        Long restaurantId = restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(employeeId);
        return orderPersistencePort.findByRestaurantAndStatus(restaurantId, status, page, size);
    }

    @Override
    public Order assignEmployee(Long orderId) {
        Long employeeId = securityContextPort.getAuthenticatedUserId();
        Long restaurantId = restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(employeeId);

        Order order = orderPersistencePort.findById(orderId);

        if (!order.getRestaurantId().equals(restaurantId))
            throw new OrderNotFromRestaurantException(
                    String.format("Order %d does not belong to restaurant %d", orderId, restaurantId)
            );

        if (!order.getStatus().equals(OrderStatus.PENDING))
            throw new OrderNotPendingException(
                    String.format("Order %d is not in PENDING status", orderId)
            );

        order.setEmployeeId(employeeId);
        order.setStatus(OrderStatus.IN_PREPARATION);

        return orderPersistencePort.update(order);
    }

    private void validateDishes(Order order) {
        order.getDishes().forEach(orderDish -> {
            Dish dish = dishPersistencePort.findById(orderDish.getDishId());

            if (!dish.getRestaurantId().equals(order.getRestaurantId()))
                throw new DishNotBelongsToRestaurantException(
                        String.format("Dish %d does not belong to restaurant %d",
                                orderDish.getDishId(), order.getRestaurantId())
                );

            if (!dish.getActive())
                throw new DishNotAvailableException(
                        String.format("Dish %d is not available", orderDish.getDishId())
                );
        });
    }
}

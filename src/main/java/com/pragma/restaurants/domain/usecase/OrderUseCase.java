package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IOrderServicePort;
import com.pragma.restaurants.domain.exception.ClientHasActiveOrderException;
import com.pragma.restaurants.domain.exception.DishNotAvailableException;
import com.pragma.restaurants.domain.exception.DishNotBelongsToRestaurantException;
import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.enums.OrderStatus;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IOrderPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        ISecurityContextPort securityContextPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.securityContextPort = securityContextPort;
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

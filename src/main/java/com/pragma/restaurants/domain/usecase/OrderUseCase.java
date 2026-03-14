package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IOrderServicePort;
import com.pragma.restaurants.domain.exception.ClientHasActiveOrderException;
import com.pragma.restaurants.domain.exception.DishNotAvailableException;
import com.pragma.restaurants.domain.exception.DishNotBelongsToRestaurantException;
import com.pragma.restaurants.domain.exception.InvalidSecurityPinException;
import com.pragma.restaurants.domain.exception.OrderNotFromRestaurantException;
import com.pragma.restaurants.domain.exception.OrderNotInPreparationException;
import com.pragma.restaurants.domain.exception.OrderNotPendingException;
import com.pragma.restaurants.domain.exception.OrderNotReadyException;
import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.enums.OrderStatus;
import com.pragma.restaurants.domain.model.events.OrderReadyEvent;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IOrderEventPort;
import com.pragma.restaurants.domain.spi.IOrderPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final ISecurityContextPort securityContextPort;
    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IOrderEventPort orderEventPort;
    private final IUserPersistencePort userPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        ISecurityContextPort securityContextPort,
                        IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                        IOrderEventPort orderEventPort,
                        IUserPersistencePort userPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.securityContextPort = securityContextPort;
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.orderEventPort = orderEventPort;
        this.userPersistencePort = userPersistencePort;
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
        Order order = findAndValidateOrderFromRestaurant(orderId);

        if (!order.getStatus().equals(OrderStatus.PENDING))
            throw new OrderNotPendingException(
                    String.format("Order %d is not in PENDING status", orderId)
            );

        order.setEmployeeId(securityContextPort.getAuthenticatedUserId());
        order.setStatus(OrderStatus.IN_PREPARATION);

        return orderPersistencePort.save(order);
//        return orderPersistencePort.update(order);
    }

    @Override
    public Order notifyOrderReady(Long orderId) {
        Order order = findAndValidateOrderFromRestaurant(orderId);

        if (!order.getStatus().equals(OrderStatus.IN_PREPARATION))
            throw new OrderNotInPreparationException(
                    String.format("Order %d is not IN_PREPARATION", orderId)
            );

        String clientPhone = userPersistencePort.getClientPhone(order.getClientId());
        String pin = generatePin();

        order.setStatus(OrderStatus.READY);
        order.setSecurityPin(pin);
        order.setClientPhone(clientPhone);

        Order updatedOrder = orderPersistencePort.save(order);

        orderEventPort.publishOrderReady(new OrderReadyEvent(
                orderId, order.getClientId(), clientPhone, pin
        ));

        return updatedOrder;
    }

    @Override
    public Order deliverOrder(Long orderId, String securityPin) {
        Order order = findAndValidateOrderFromRestaurant(orderId);

        if (!order.getStatus().equals(OrderStatus.READY))
            throw new OrderNotReadyException(
                    String.format("Order %d is not in READY status", orderId)
            );

        if (!order.getSecurityPin().equals(securityPin))
            throw new InvalidSecurityPinException(
                    String.format("Invalid security pin for order %d", orderId)
            );

        order.setStatus(OrderStatus.DELIVERED);
        return orderPersistencePort.save(order);
    }

    private Order findAndValidateOrderFromRestaurant(Long orderId) {
        Long employeeId = securityContextPort.getAuthenticatedUserId();
        Long restaurantId = restaurantEmployeePersistencePort
                .findRestaurantIdByEmployeeId(employeeId);

        Order order = orderPersistencePort.findById(orderId);

        if (!order.getRestaurantId().equals(restaurantId))
            throw new OrderNotFromRestaurantException(
                    String.format("Order %d does not belong to restaurant %d",
                            orderId, restaurantId)
            );

        return order;
    }

    private String generatePin() {
        return String.format("%06d", (int) (Math.random() * 1000000));
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

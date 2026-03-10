package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IRestaurantEmployeeServicePort;
import com.pragma.restaurants.domain.exception.UserIsNotOwnerException;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.model.RestaurantEmployee;
import com.pragma.restaurants.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;

public class RestaurantEmployeeUseCase implements IRestaurantEmployeeServicePort {

    private final IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public RestaurantEmployeeUseCase(IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort,
                                     IRestaurantPersistencePort restaurantPersistencePort,
                                     ISecurityContextPort securityContextPort) {
        this.restaurantEmployeePersistencePort = restaurantEmployeePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public RestaurantEmployee save(RestaurantEmployee restaurantEmployee) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantEmployee.getRestaurantId());

        Long authenticatedUserId = securityContextPort.getAuthenticatedUserId();
        if (!restaurant.getOwnerId().equals(authenticatedUserId)) {
            throw new UserIsNotOwnerException(
                    String.format("User %d is not the owner of restaurant %d",
                            authenticatedUserId, restaurantEmployee.getRestaurantId())
            );
        }

        return restaurantEmployeePersistencePort.save(restaurantEmployee);
    }
}

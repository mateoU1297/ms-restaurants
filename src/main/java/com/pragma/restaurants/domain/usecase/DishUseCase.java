package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IDishServicePort;
import com.pragma.restaurants.domain.exception.UserIsNotOwnerException;
import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ISecurityContextPort securityContextPort;

    public DishUseCase(IDishPersistencePort dishPersistencePort,
                       IRestaurantPersistencePort restaurantPersistencePort,
                       ISecurityContextPort securityContextPort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.securityContextPort = securityContextPort;
    }

    @Override
    public Dish save(Dish dish) {
        validateOwnership(dish.getRestaurantId());
        dish.setActive(true);
        return dishPersistencePort.save(dish);
    }

    @Override
    public Dish update(Long dishId, Dish dish) {
        Dish dishExisting = dishPersistencePort.findById(dishId);
        validateOwnership(dishExisting.getRestaurantId());

        dishExisting.setPrice(dish.getPrice());
        dishExisting.setDescription(dish.getDescription());

        return dishPersistencePort.update(dishExisting);
    }

    @Override
    public Dish toggleActive(Long dishId) {
        Dish existing = dishPersistencePort.findById(dishId);
        validateOwnership(existing.getRestaurantId());
        existing.setActive(!existing.getActive());
        return dishPersistencePort.update(existing);
    }

    @Override
    public Page<Dish> findByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        return dishPersistencePort.findByRestaurant(restaurantId, categoryId, page, size);
    }

    private void validateOwnership(Long restaurantId) {
        Restaurant restaurant = restaurantPersistencePort.findById(restaurantId);
        Long authenticatedUserId = securityContextPort.getAuthenticatedUserId();

        if (!restaurant.getOwnerId().equals(authenticatedUserId)) {
            throw new UserIsNotOwnerException(
                    String.format("User %d is not the owner of restaurant %d",
                            authenticatedUserId, restaurantId)
            );
        }
    }
}

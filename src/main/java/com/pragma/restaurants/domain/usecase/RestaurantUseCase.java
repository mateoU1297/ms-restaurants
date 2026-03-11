package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IRestaurantServicePort;
import com.pragma.restaurants.domain.exception.UserIsNotOwnerException;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;

public class RestaurantUseCase implements IRestaurantServicePort {

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userPersistencePort;

    private static final String OWNER = "OWNER";

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                             IUserPersistencePort userPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        boolean isOwner = userPersistencePort.getUserById(restaurant.getOwnerId())
                .getRoles()
                .contains(OWNER);

        if (!isOwner)
            throw new UserIsNotOwnerException(String.format("User %d is not an owner", restaurant.getOwnerId()));

        return restaurantPersistencePort.save(restaurant);
    }

    @Override
    public Page<Restaurant> findAll(int page, int size) {
        return restaurantPersistencePort.findAll(page, size);
    }
}

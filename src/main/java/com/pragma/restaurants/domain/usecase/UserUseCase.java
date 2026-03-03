package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IUserServicePort;
import com.pragma.restaurants.domain.model.User;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public Boolean validateOwner(Long userId) {
        return userPersistencePort.validateOwner(userId);
    }

    @Override
    public User getUserById(Long userId) {
        return userPersistencePort.getUserById(userId);
    }
}

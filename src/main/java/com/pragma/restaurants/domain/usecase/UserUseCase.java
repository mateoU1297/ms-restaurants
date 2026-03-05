package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.api.IUserServicePort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;

    private final String ADMIN = "ADMIN";

    public UserUseCase(IUserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public Boolean isAdmin(Long userId) {
        var response = userPersistencePort.getUserById(userId)
                .getRoles()
                .contains(ADMIN);

        if (!response)
            throw new RuntimeException(String.format("User with id:%d is not admin", userId));

        return true;
    }
}

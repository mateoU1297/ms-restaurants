package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.User;

public interface IUserPersistencePort {

    Boolean validateOwner(Long userId);

    User getUserById(Long userId);
}

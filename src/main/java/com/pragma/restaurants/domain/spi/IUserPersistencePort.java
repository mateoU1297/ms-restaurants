package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.User;

public interface IUserPersistencePort {

    User getUserById(Long userId);
}

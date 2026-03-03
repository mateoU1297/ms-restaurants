package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.User;

public interface IUserServicePort {

    Boolean validateOwner(Long userId);

    User getUserById(Long userId);
}

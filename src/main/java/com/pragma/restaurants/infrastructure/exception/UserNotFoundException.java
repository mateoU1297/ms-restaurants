package com.pragma.restaurants.infrastructure.exception;

public class UserNotFoundException extends InfrastructureException {

    public UserNotFoundException(Long id) {
        super("USER_NOT_FOUND",
                String.format("User with id: %s not found", id));
    }
}

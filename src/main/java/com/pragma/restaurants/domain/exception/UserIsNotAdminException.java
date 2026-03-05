package com.pragma.restaurants.domain.exception;

public class UserIsNotAdminException extends DomainException {

    public UserIsNotAdminException(String message) {
        super("USER_IS_NOT_ADMIN", message);
    }

}

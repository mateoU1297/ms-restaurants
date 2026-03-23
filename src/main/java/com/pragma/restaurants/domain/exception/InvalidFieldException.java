package com.pragma.restaurants.domain.exception;

public class InvalidFieldException extends DomainException {

    public InvalidFieldException(String message) {
        super("INVALID_FIELD", message);
    }
}
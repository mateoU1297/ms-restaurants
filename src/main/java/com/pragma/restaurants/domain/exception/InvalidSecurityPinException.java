package com.pragma.restaurants.domain.exception;

public class InvalidSecurityPinException extends DomainException {

    public InvalidSecurityPinException(String message) {
        super("INVALID_SECURITY_PIN", message);
    }
}
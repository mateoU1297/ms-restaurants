package com.pragma.restaurants.domain.exception;

public class ClientHasActiveOrderException extends DomainException {

    public ClientHasActiveOrderException(String message) {
        super("CLIENT_HAS_ACTIVE_ORDER", message);
    }
}

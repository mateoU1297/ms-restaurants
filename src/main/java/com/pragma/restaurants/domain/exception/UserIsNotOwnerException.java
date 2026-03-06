package com.pragma.restaurants.domain.exception;

public class UserIsNotOwnerException extends DomainException {

  public UserIsNotOwnerException(String message) {
    super("USER_IS_NOT_OWNER", message);
  }
}

package com.pragma.restaurants.domain.spi;

public interface ISecurityContextPort {

    Long getAuthenticatedUserId();

    String getAuthenticatedUserRoles();
}

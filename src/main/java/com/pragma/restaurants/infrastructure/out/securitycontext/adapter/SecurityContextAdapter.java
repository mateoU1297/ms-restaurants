package com.pragma.restaurants.infrastructure.out.securitycontext.adapter;

import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

@RequiredArgsConstructor
public class SecurityContextAdapter implements ISecurityContextPort {

    @Override
    public Long getAuthenticatedUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return jwt.getClaim("id");
    }

    @Override
    public String getAuthenticatedUserRoles() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return jwt.getClaim("roles");
    }
}

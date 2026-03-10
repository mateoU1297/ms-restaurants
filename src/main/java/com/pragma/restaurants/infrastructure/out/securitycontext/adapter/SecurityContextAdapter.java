package com.pragma.restaurants.infrastructure.out.securitycontext.adapter;

import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecurityContextAdapter implements ISecurityContextPort {

    private final HttpServletRequest httpServletRequest;

    @Override
    public Long getAuthenticatedUserId() {
        String userId = httpServletRequest.getHeader("X-User-Id");
        if (userId == null) {
            throw new RuntimeException("X-User-Id header not found");
        }
        return Long.parseLong(userId);
    }

    @Override
    public String getAuthenticatedUserRoles() {
        String roles = httpServletRequest.getHeader("X-User-Roles");
        if (roles == null) {
            throw new RuntimeException("X-User-Roles header not found");
        }
        return roles;
    }
}

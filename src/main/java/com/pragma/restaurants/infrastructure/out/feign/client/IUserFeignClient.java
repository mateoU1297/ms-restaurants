package com.pragma.restaurants.infrastructure.out.feign.client;

import com.pragma.restaurants.application.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-users")
public interface IUserFeignClient {

    @GetMapping("/api/v1/users/admin/user/{userId}")
    UserResponse getUserById(@PathVariable("userId") Long userId);
}

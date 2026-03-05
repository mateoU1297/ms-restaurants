package com.pragma.restaurants.infrastructure.out.feign.adapter;

import com.pragma.restaurants.application.dto.response.UserResponse;
import com.pragma.restaurants.domain.model.User;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;
import com.pragma.restaurants.infrastructure.mapper.IUserFeignMapper;
import com.pragma.restaurants.infrastructure.out.feign.client.IUserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFeignAdapter implements IUserPersistencePort {

    private final IUserFeignClient userFeignClient;
    private final IUserFeignMapper userFeignMapper;

    @Override
    public User getUserById(Long userId) {
        UserResponse response = userFeignClient.getUserById(userId);
        log.info("userFeignClient.getUserById userId={} response={}", userId, response);
        return userFeignMapper.toDomain(response);
    }
}
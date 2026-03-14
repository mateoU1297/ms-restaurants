package com.pragma.restaurants.infrastructure.out.feign.adapter;

import com.pragma.restaurants.domain.model.User;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;
import com.pragma.restaurants.infrastructure.mapper.IUserFeignMapper;
import com.pragma.restaurants.infrastructure.out.feign.client.IUserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFeignAdapter implements IUserPersistencePort {

    private final IUserFeignClient userFeignClient;
    private final IUserFeignMapper userFeignMapper;

    @Override
    public User getUserById(Long userId) {
        return userFeignMapper.toDomain(userFeignClient.getUserById(userId));
    }

    @Override
    public String getClientPhone(Long clientId) {
        return userFeignMapper.toDomain(userFeignClient.getUserById(clientId)).getPhoneNumber();
    }
}
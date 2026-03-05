package com.pragma.restaurants.infrastructure.config;

import com.pragma.restaurants.domain.api.IRestaurantServicePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;
import com.pragma.restaurants.domain.usecase.RestaurantUseCase;
import com.pragma.restaurants.infrastructure.mapper.IRestaurantEntityMapper;
import com.pragma.restaurants.infrastructure.mapper.IUserFeignMapper;
import com.pragma.restaurants.infrastructure.out.feign.adapter.UserFeignAdapter;
import com.pragma.restaurants.infrastructure.out.feign.client.IUserFeignClient;
import com.pragma.restaurants.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.restaurants.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final RestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    private final IUserFeignClient userFeignClient;
    private final IUserFeignMapper userFeignMapper;

    @Bean
    public IRestaurantPersistencePort restaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantRepository, restaurantEntityMapper);
    }

    @Bean
    public IRestaurantServicePort restaurantServicePort() {
        return new RestaurantUseCase(restaurantPersistencePort(), userPersistencePort());
    }

    @Bean
    public IUserPersistencePort userPersistencePort() {
        return new UserFeignAdapter(userFeignClient, userFeignMapper);
    }

}

package com.pragma.restaurants.infrastructure.config;

import com.pragma.restaurants.domain.api.IDishServicePort;
import com.pragma.restaurants.domain.api.IRestaurantServicePort;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;
import com.pragma.restaurants.domain.usecase.DishUseCase;
import com.pragma.restaurants.domain.usecase.RestaurantUseCase;
import com.pragma.restaurants.infrastructure.mapper.IDishEntityMapper;
import com.pragma.restaurants.infrastructure.mapper.IRestaurantEntityMapper;
import com.pragma.restaurants.infrastructure.mapper.IUserFeignMapper;
import com.pragma.restaurants.infrastructure.out.feign.adapter.UserFeignAdapter;
import com.pragma.restaurants.infrastructure.out.feign.client.IUserFeignClient;
import com.pragma.restaurants.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.pragma.restaurants.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.restaurants.infrastructure.out.securitycontext.adapter.SecurityContextAdapter;
import com.pragma.restaurants.infrastructure.repository.DishRepository;
import com.pragma.restaurants.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    private final RestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final DishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

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

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishRepository, dishEntityMapper);
    }

    @Bean
    public ISecurityContextPort securityContextPort() {
        return new SecurityContextAdapter();
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), restaurantPersistencePort(), securityContextPort());
    }

}

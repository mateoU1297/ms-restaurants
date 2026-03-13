package com.pragma.restaurants.infrastructure.config;

import com.pragma.restaurants.domain.api.IDishServicePort;
import com.pragma.restaurants.domain.api.IOrderServicePort;
import com.pragma.restaurants.domain.api.IRestaurantEmployeeServicePort;
import com.pragma.restaurants.domain.api.IRestaurantServicePort;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IOrderPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;
import com.pragma.restaurants.domain.usecase.DishUseCase;
import com.pragma.restaurants.domain.usecase.OrderUseCase;
import com.pragma.restaurants.domain.usecase.RestaurantEmployeeUseCase;
import com.pragma.restaurants.domain.usecase.RestaurantUseCase;
import com.pragma.restaurants.infrastructure.mapper.IDishEntityMapper;
import com.pragma.restaurants.infrastructure.mapper.IOrderEntityMapper;
import com.pragma.restaurants.infrastructure.mapper.IRestaurantEmployeeEntityMapper;
import com.pragma.restaurants.infrastructure.mapper.IRestaurantEntityMapper;
import com.pragma.restaurants.infrastructure.mapper.IUserFeignMapper;
import com.pragma.restaurants.infrastructure.out.feign.adapter.UserFeignAdapter;
import com.pragma.restaurants.infrastructure.out.feign.client.IUserFeignClient;
import com.pragma.restaurants.infrastructure.out.jpa.adapter.DishJpaAdapter;
import com.pragma.restaurants.infrastructure.out.jpa.adapter.OrderJpaAdapter;
import com.pragma.restaurants.infrastructure.out.jpa.adapter.RestaurantEmployeeJpaAdapter;
import com.pragma.restaurants.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.restaurants.infrastructure.out.securitycontext.adapter.SecurityContextAdapter;
import com.pragma.restaurants.infrastructure.repository.DishRepository;
import com.pragma.restaurants.infrastructure.repository.OrderRepository;
import com.pragma.restaurants.infrastructure.repository.RestaurantEmployeeRepository;
import com.pragma.restaurants.infrastructure.repository.RestaurantRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;
    private final OrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    private final HttpServletRequest httpServletRequest;

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
        return new SecurityContextAdapter(httpServletRequest);
    }

    @Bean
    public IDishServicePort dishServicePort() {
        return new DishUseCase(dishPersistencePort(), restaurantPersistencePort(), securityContextPort());
    }

    @Bean
    public IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort() {
        return new RestaurantEmployeeJpaAdapter(restaurantEmployeeRepository, restaurantEmployeeEntityMapper);
    }

    @Bean
    public IRestaurantEmployeeServicePort restaurantEmployeeServicePort() {
        return new RestaurantEmployeeUseCase(restaurantEmployeePersistencePort(), restaurantPersistencePort(),
                securityContextPort());
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort() {
        return new OrderJpaAdapter(orderRepository, orderEntityMapper);
    }

    @Bean
    public IOrderServicePort orderServicePort() {
        return new OrderUseCase(orderPersistencePort(), restaurantPersistencePort(), dishPersistencePort(),
                securityContextPort(), restaurantEmployeePersistencePort());
    }

}

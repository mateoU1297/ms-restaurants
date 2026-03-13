package com.pragma.restaurants.infrastructure.out.jpa.adapter;

import com.pragma.restaurants.domain.model.RestaurantEmployee;
import com.pragma.restaurants.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.restaurants.infrastructure.exception.EmployeeNotAssignedException;
import com.pragma.restaurants.infrastructure.mapper.IRestaurantEmployeeEntityMapper;
import com.pragma.restaurants.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import com.pragma.restaurants.infrastructure.repository.RestaurantEmployeeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantEmployeeJpaAdapter implements IRestaurantEmployeePersistencePort {

    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;

    @Override
    public RestaurantEmployee save(RestaurantEmployee restaurantEmployee) {
        RestaurantEmployeeEntity entity = restaurantEmployeeEntityMapper.toEntity(restaurantEmployee);
        return restaurantEmployeeEntityMapper.toDomain(restaurantEmployeeRepository.save(entity));
    }

    @Override
    public Long findRestaurantIdByEmployeeId(Long employeeId) {
        return restaurantEmployeeRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new EmployeeNotAssignedException(
                        String.format("Employee %d is not assigned to any restaurant", employeeId)
                ))
                .getRestaurantId();
    }
}
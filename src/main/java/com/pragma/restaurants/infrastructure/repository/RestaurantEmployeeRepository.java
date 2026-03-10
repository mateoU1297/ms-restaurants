package com.pragma.restaurants.infrastructure.repository;

import com.pragma.restaurants.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantEmployeeRepository extends JpaRepository<RestaurantEmployeeEntity, Long> {
}

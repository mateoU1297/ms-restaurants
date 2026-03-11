package com.pragma.restaurants.infrastructure.repository;

import com.pragma.restaurants.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Long> {

    @Query("SELECT d FROM DishEntity d WHERE d.restaurant.id = :restaurantId " +
            "AND (:categoryId IS NULL OR d.category.id = :categoryId) " +
            "AND d.active = true")
    Page<DishEntity> findByRestaurantAndCategory(
            @Param("restaurantId") Long restaurantId,
            @Param("categoryId") Long categoryId,
            Pageable pageable);
}

package com.pragma.restaurants.domain.model;

import java.time.LocalDateTime;

public class RestaurantEmployee {
    private Long id;
    private Long employeeId;
    private Long restaurantId;
    private LocalDateTime createdAt;

    public RestaurantEmployee() {
    }

    public RestaurantEmployee(Long id, Long employeeId, Long restaurantId, LocalDateTime createdAt) {
        this.id = id;
        this.employeeId = employeeId;
        this.restaurantId = restaurantId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

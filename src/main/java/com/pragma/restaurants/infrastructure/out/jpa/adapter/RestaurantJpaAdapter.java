package com.pragma.restaurants.infrastructure.out.jpa.adapter;

import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.infrastructure.mapper.IRestaurantEntityMapper;
import com.pragma.restaurants.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.restaurants.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final RestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.toEntity(restaurant);

        return restaurantEntityMapper.toRestaurant(restaurantRepository.save(restaurantEntity));
    }

    @Override
    public Restaurant findById(Long id) {
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return restaurantEntityMapper.toRestaurant(restaurantEntity);
    }
}

package com.pragma.restaurants.infrastructure.out.jpa.adapter;

import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.infrastructure.mapper.IRestaurantEntityMapper;
import com.pragma.restaurants.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.restaurants.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Page<Restaurant> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        org.springframework.data.domain.Page<RestaurantEntity> springPage =
                restaurantRepository.findAll(pageable);

        List<Restaurant> restaurants = springPage.getContent()
                .stream()
                .map(restaurantEntityMapper::toRestaurant)
                .collect(Collectors.toList());

        return new Page<>(
                restaurants,
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.isLast()
        );
    }
}

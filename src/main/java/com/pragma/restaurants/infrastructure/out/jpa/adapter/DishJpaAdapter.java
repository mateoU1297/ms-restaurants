package com.pragma.restaurants.infrastructure.out.jpa.adapter;

import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.infrastructure.exception.DishNotFoundException;
import com.pragma.restaurants.infrastructure.mapper.IDishEntityMapper;
import com.pragma.restaurants.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.restaurants.infrastructure.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final DishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public Dish save(Dish dish) {
        DishEntity entity = dishEntityMapper.toEntity(dish);
        return dishEntityMapper.toDomain(dishRepository.save(entity));
    }

    @Override
    public Dish findById(Long id) {
        return dishEntityMapper.toDomain(
                dishRepository.findById(id)
                        .orElseThrow(() -> new DishNotFoundException(id))
        );
    }

    @Override
    public Page<Dish> findByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        org.springframework.data.domain.Page<DishEntity> springPage =
                dishRepository.findByRestaurantAndCategory(restaurantId, categoryId, pageable);

        List<Dish> dishes = springPage.getContent()
                .stream()
                .map(dishEntityMapper::toDomain)
                .collect(Collectors.toList());

        return new Page<>(
                dishes,
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages(),
                springPage.isLast()
        );
    }
}
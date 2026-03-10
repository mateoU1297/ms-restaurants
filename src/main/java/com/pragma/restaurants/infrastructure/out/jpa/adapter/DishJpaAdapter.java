package com.pragma.restaurants.infrastructure.out.jpa.adapter;

import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.infrastructure.exception.DishNotFoundException;
import com.pragma.restaurants.infrastructure.mapper.IDishEntityMapper;
import com.pragma.restaurants.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.restaurants.infrastructure.repository.DishRepository;
import lombok.RequiredArgsConstructor;

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
    public Dish update(Dish dish) {
        DishEntity entity = dishEntityMapper.toEntity(dish);
        return dishEntityMapper.toDomain(dishRepository.save(entity));
    }
}
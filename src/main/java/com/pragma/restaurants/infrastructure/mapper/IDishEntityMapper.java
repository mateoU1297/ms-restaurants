package com.pragma.restaurants.infrastructure.mapper;

import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.infrastructure.out.jpa.entity.DishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishEntityMapper {

    @Mapping(target = "restaurant.id", source = "restaurantId")
    @Mapping(target = "category.id", source = "categoryId")
    DishEntity toEntity(Dish dish);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "categoryId", source = "category.id")
    Dish toDomain(DishEntity dishEntity);
}

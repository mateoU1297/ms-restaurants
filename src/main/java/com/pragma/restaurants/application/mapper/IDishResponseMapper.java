package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.DishResponse;
import com.pragma.restaurants.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {

    @Mapping(target = "categoryId", source = "categoryId")
    @Mapping(target = "restaurantId", source = "restaurantId")
    DishResponse toResponse(Dish dish);
}
package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.DishUpdateRequest;
import com.pragma.restaurants.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishUpdateRequestMapper {

    Dish toDomain(DishUpdateRequest dishUpdateRequest);
}

package com.pragma.restaurants.infrastructure.mapper;

import com.pragma.restaurants.domain.model.RestaurantEmployee;
import com.pragma.restaurants.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEmployeeEntityMapper {

    RestaurantEmployeeEntity toEntity(RestaurantEmployee restaurantEmployee);

    RestaurantEmployee toDomain(RestaurantEmployeeEntity restaurantEmployeeEntity);
}

package com.pragma.restaurants.infrastructure.mapper;

import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.infrastructure.out.jpa.entity.RestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEntityMapper {

    Restaurant toRestaurant(RestaurantEntity restaurantEntity);

    RestaurantEntity toEntity(Restaurant restaurant);
}

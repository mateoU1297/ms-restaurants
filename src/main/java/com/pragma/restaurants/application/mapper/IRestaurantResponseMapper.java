package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.RestaurantResponse;
import com.pragma.restaurants.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {

    RestaurantResponse toResponse(Restaurant restaurant);
}
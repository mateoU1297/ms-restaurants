package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.RestaurantEmployeeRequest;
import com.pragma.restaurants.domain.model.RestaurantEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEmployeeRequestMapper {

    RestaurantEmployee toDomain(RestaurantEmployeeRequest restaurantEmployeeRequest);
}

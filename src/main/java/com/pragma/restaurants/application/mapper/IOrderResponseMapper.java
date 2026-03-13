package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.OrderDishResponse;
import com.pragma.restaurants.application.dto.OrderResponse;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {

    @Mapping(target = "dishes", source = "dishes")
    OrderResponse toResponse(Order order);

    @Mapping(target = "dishId", source = "dishId")
    @Mapping(target = "quantity", source = "quantity")
    OrderDishResponse toResponse(OrderDish orderDish);
}
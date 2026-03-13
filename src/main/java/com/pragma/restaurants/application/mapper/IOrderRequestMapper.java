package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.OrderDishRequest;
import com.pragma.restaurants.application.dto.OrderRequest;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.OrderDish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper {

    @Mapping(target = "dishes", source = "dishes")
    Order toDomain(OrderRequest orderRequest);

    @Mapping(target = "dishId", source = "dishId")
    @Mapping(target = "quantity", source = "quantity")
    OrderDish toDomain(OrderDishRequest orderDishRequest);
}

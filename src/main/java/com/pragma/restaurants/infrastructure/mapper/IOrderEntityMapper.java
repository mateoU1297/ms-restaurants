package com.pragma.restaurants.infrastructure.mapper;

import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.OrderDish;
import com.pragma.restaurants.infrastructure.out.jpa.entity.OrderDishEntity;
import com.pragma.restaurants.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {

    @Mapping(target = "dishes", source = "dishes")
    OrderEntity toEntity(Order order);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "dishId", source = "dishId")
    @Mapping(target = "quantity", source = "quantity")
    OrderDishEntity toEntity(OrderDish orderDish);

    @Mapping(target = "dishes", source = "dishes")
    Order toDomain(OrderEntity orderEntity);

    @Mapping(target = "dishId", source = "dishId")
    @Mapping(target = "quantity", source = "quantity")
    OrderDish toDomain(OrderDishEntity orderDishEntity);
}
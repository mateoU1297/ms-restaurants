package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.OrderResponse;
import com.pragma.restaurants.application.dto.PagedOrderResponse;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.Page;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderPageMapper {

    default PagedOrderResponse toResponse(Page<Order> page) {
        PagedOrderResponse response = new PagedOrderResponse();
        response.setContent(page.getContent()
                .stream()
                .map(this::toOrderResponse)
                .collect(Collectors.toList()));
        response.setPageNumber(page.getPageNumber());
        response.setPageSize(page.getPageSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }

    OrderResponse toOrderResponse(Order order);
}
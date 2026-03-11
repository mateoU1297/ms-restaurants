package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.DishSummaryResponse;
import com.pragma.restaurants.application.dto.PagedDishResponse;
import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Page;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishPageMapper {

    DishSummaryResponse toSummaryResponse(Dish dish);

    default PagedDishResponse toResponse(Page<Dish> page) {
        PagedDishResponse response = new PagedDishResponse();
        response.setContent(page.getContent()
                .stream()
                .map(this::toSummaryResponse)
                .collect(Collectors.toList()));
        response.setPageNumber(page.getPageNumber());
        response.setPageSize(page.getPageSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        return response;
    }
}

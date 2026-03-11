package com.pragma.restaurants.application.mapper;

import com.pragma.restaurants.application.dto.PagedRestaurantResponse;
import com.pragma.restaurants.application.dto.RestaurantSummaryResponse;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantPageMapper {

    RestaurantSummaryResponse toSummaryResponse(Restaurant restaurant);

    default PagedRestaurantResponse toResponse(Page<Restaurant> page) {
        PagedRestaurantResponse response = new PagedRestaurantResponse();
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

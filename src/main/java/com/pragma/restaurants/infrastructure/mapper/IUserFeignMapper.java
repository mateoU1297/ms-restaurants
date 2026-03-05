package com.pragma.restaurants.infrastructure.mapper;

import com.pragma.restaurants.application.dto.response.UserResponse;
import com.pragma.restaurants.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserFeignMapper {

    User toDomain(UserResponse userResponse);
}

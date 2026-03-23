package com.pragma.restaurants.domain.model.validator;

import com.pragma.restaurants.domain.exception.InvalidFieldException;
import com.pragma.restaurants.domain.model.Restaurant;

public class RestaurantValidator {

    private RestaurantValidator() {}

    public static void validate(Restaurant restaurant) {
        validateName(restaurant.getName());
        validateNit(restaurant.getNit());
        validatePhone(restaurant.getPhone());
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank())
            throw new InvalidFieldException("El nombre del restaurante no puede estar vacío");
        if (name.matches("^[0-9]+$"))
            throw new InvalidFieldException(
                    "El nombre del restaurante no puede contener solo números"
            );
    }

    private static void validateNit(String nit) {
        if (nit == null || !nit.matches("^[0-9]+$"))
            throw new InvalidFieldException("El NIT debe ser únicamente numérico");
    }

    private static void validatePhone(String phone) {
        if (phone == null || !phone.matches("^\\+?[0-9]{7,13}$"))
            throw new InvalidFieldException(
                    "Teléfono inválido. Máximo 13 caracteres, puede iniciar con +"
            );
    }
}
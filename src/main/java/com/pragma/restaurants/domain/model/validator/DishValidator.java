package com.pragma.restaurants.domain.model.validator;

import com.pragma.restaurants.domain.exception.InvalidFieldException;
import com.pragma.restaurants.domain.model.Dish;

public class DishValidator {

    private DishValidator() {}

    public static void validate(Dish dish) {
        validatePrice(dish.getPrice());
    }

    private static void validatePrice(Integer price) {
        if (price == null || price <= 0)
            throw new InvalidFieldException(
                    "El precio del plato debe ser un número entero positivo mayor a 0"
            );
    }
}
package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.Restaurant;

public interface IRestaurantServicePort {

    Restaurant save(Restaurant restaurant);
}

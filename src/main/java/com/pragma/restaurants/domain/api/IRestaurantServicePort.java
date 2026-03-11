package com.pragma.restaurants.domain.api;

import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;

public interface IRestaurantServicePort {

    Restaurant save(Restaurant restaurant);

    Page<Restaurant> findAll(int page, int size);
}

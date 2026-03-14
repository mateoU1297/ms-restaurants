package com.pragma.restaurants.domain.spi;

import com.pragma.restaurants.domain.model.events.OrderReadyEvent;

public interface IOrderEventPort {

    void publishOrderReady(OrderReadyEvent event);
}

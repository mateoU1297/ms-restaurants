package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.exception.UserIsNotOwnerException;
import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistencePort;
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwnerId(10L);

        dish = new Dish();
        dish.setRestaurantId(1L);
        dish.setName("Hamburguesa Clásica");
        dish.setPrice(25000);
        dish.setDescription("Hamburguesa con carne de res");
        dish.setUrlImage("https://storage.com/hamburguesa.png");
        dish.setCategoryId(1L);
    }

    @Test
    void save_whenUserIsOwner_shouldSetActiveAndSaveDish() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.save(dish)).thenReturn(dish);

        Dish result = dishUseCase.save(dish);

        assertNotNull(result);
        assertTrue(result.getActive());
        verify(dishPersistencePort).save(dish);
    }

    @Test
    void save_whenUserIsOwner_shouldAlwaysSetActiveTrue() {
        dish.setActive(false);

        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.save(dish)).thenReturn(dish);

        dishUseCase.save(dish);

        assertTrue(dish.getActive());
    }

    @Test
    void save_whenUserIsNotOwner_shouldThrowUserIsNotOwnerException() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> dishUseCase.save(dish));

        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void save_whenUserIsNotOwner_shouldNotPersistDish() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> dishUseCase.save(dish));

        verify(dishPersistencePort, never()).save(any());
    }

    @Test
    void save_shouldFetchRestaurantWithCorrectId() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.save(any())).thenReturn(dish);

        dishUseCase.save(dish);

        verify(restaurantPersistencePort).findById(1L);
    }

    @Test
    void save_shouldGetAuthenticatedUserIdFromSecurityContext() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.save(any())).thenReturn(dish);

        dishUseCase.save(dish);

        verify(securityContextPort).getAuthenticatedUserId();
    }
}
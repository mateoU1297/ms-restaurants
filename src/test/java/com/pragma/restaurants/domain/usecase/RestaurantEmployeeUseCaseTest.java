package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.exception.UserIsNotOwnerException;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.model.RestaurantEmployee;
import com.pragma.restaurants.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantEmployeeUseCaseTest {

    @Mock
    private IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private ISecurityContextPort securityContextPort;

    @InjectMocks
    private RestaurantEmployeeUseCase restaurantEmployeeUseCase;

    private Restaurant restaurant;
    private RestaurantEmployee restaurantEmployee;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwnerId(10L);

        restaurantEmployee = new RestaurantEmployee();
        restaurantEmployee.setEmployeeId(20L);
        restaurantEmployee.setRestaurantId(1L);
    }

    @Test
    void save_whenUserIsOwner_shouldSaveAndReturnRestaurantEmployee() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(restaurantEmployeePersistencePort.save(restaurantEmployee)).thenReturn(restaurantEmployee);

        RestaurantEmployee result = restaurantEmployeeUseCase.save(restaurantEmployee);

        assertNotNull(result);
        assertEquals(restaurantEmployee, result);
        verify(restaurantEmployeePersistencePort).save(restaurantEmployee);
    }

    @Test
    void save_whenUserIsNotOwner_shouldThrowUserIsNotOwnerException() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> restaurantEmployeeUseCase.save(restaurantEmployee));

        verifyNoInteractions(restaurantEmployeePersistencePort);
    }

    @Test
    void save_whenUserIsNotOwner_shouldNotPersistRelation() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> restaurantEmployeeUseCase.save(restaurantEmployee));

        verify(restaurantEmployeePersistencePort, never()).save(any());
    }

    @Test
    void save_shouldFetchRestaurantWithCorrectId() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(restaurantEmployeePersistencePort.save(any())).thenReturn(restaurantEmployee);

        restaurantEmployeeUseCase.save(restaurantEmployee);

        verify(restaurantPersistencePort).findById(1L);
    }

    @Test
    void save_shouldGetAuthenticatedUserIdFromSecurityContext() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(restaurantEmployeePersistencePort.save(any())).thenReturn(restaurantEmployee);

        restaurantEmployeeUseCase.save(restaurantEmployee);

        verify(securityContextPort).getAuthenticatedUserId();
    }
}
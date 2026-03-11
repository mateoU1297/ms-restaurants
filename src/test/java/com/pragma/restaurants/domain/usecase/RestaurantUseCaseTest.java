package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.exception.UserIsNotOwnerException;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.model.User;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant restaurant;
    private User ownerUser;
    private User nonOwnerUser;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwnerId(10L);
        restaurant.setName("Test Restaurant");

        ownerUser = new User();
        ownerUser.setId(10L);
        ownerUser.setRoles(Set.of("OWNER"));

        nonOwnerUser = new User();
        nonOwnerUser.setId(10L);
        nonOwnerUser.setRoles(Set.of("ADMIN"));
    }

    @Test
    void save_whenOwnerIsAdmin_shouldSaveAndReturnRestaurant() {
        when(userPersistencePort.getUserById(10L)).thenReturn(ownerUser);
        when(restaurantPersistencePort.save(restaurant)).thenReturn(restaurant);

        Restaurant result = restaurantUseCase.save(restaurant);

        assertNotNull(result);
        assertEquals(restaurant, result);
        verify(restaurantPersistencePort).save(restaurant);
    }

    @Test
    void save_whenOwnerIsNotAdmin_shouldThrowUserIsNotOwnerException() {
        when(userPersistencePort.getUserById(10L)).thenReturn(nonOwnerUser);

        assertThrows(UserIsNotOwnerException.class,
                () -> restaurantUseCase.save(restaurant));

        verifyNoInteractions(restaurantPersistencePort);
    }

    @Test
    void save_whenOwnerIsNotAdmin_shouldNotPersistRestaurant() {
        when(userPersistencePort.getUserById(10L)).thenReturn(nonOwnerUser);

        assertThrows(UserIsNotOwnerException.class,
                () -> restaurantUseCase.save(restaurant));

        verify(restaurantPersistencePort, never()).save(any());
    }

    @Test
    void save_shouldFetchUserWithCorrectOwnerId() {
        when(userPersistencePort.getUserById(10L)).thenReturn(ownerUser);
        when(restaurantPersistencePort.save(any())).thenReturn(restaurant);

        restaurantUseCase.save(restaurant);

        verify(userPersistencePort).getUserById(10L);
    }

    @Test
    void findAll_shouldReturnPagedRestaurants() {
        List<Restaurant> restaurants = List.of(restaurant);
        Page<Restaurant> expectedPage = new Page<>(restaurants, 0, 10, 1L, 1, true);

        when(restaurantPersistencePort.findAll(0, 10)).thenReturn(expectedPage);

        Page<Restaurant> result = restaurantUseCase.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isLast());
    }

    @Test
    void findAll_shouldDelegateToPeristencePort() {
        Page<Restaurant> expectedPage = new Page<>(List.of(), 0, 10, 0L, 0, true);
        when(restaurantPersistencePort.findAll(0, 10)).thenReturn(expectedPage);

        restaurantUseCase.findAll(0, 10);

        verify(restaurantPersistencePort).findAll(0, 10);
    }

    @Test
    void findAll_shouldNotInteractWithUserPort() {
        Page<Restaurant> expectedPage = new Page<>(List.of(), 0, 10, 0L, 0, true);
        when(restaurantPersistencePort.findAll(0, 10)).thenReturn(expectedPage);

        restaurantUseCase.findAll(0, 10);

        verifyNoInteractions(userPersistencePort);
    }

    @Test
    void findAll_shouldPassCorrectPageAndSizeParameters() {
        Page<Restaurant> expectedPage = new Page<>(List.of(), 2, 5, 0L, 0, true);
        when(restaurantPersistencePort.findAll(2, 5)).thenReturn(expectedPage);

        restaurantUseCase.findAll(2, 5);

        verify(restaurantPersistencePort).findAll(2, 5);
    }
}
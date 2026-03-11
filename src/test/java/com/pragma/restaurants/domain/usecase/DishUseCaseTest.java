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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

    @Test
    void update_whenUserIsOwner_shouldUpdateOnlyPriceAndDescription() {
        Dish existing = buildExistingDish();
        Dish updateRequest = new Dish();
        updateRequest.setPrice(35000);
        updateRequest.setDescription("Descripción actualizada");

        when(dishPersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.update(existing)).thenReturn(existing);

        Dish result = dishUseCase.update(1L, updateRequest);

        assertEquals(35000, result.getPrice());
        assertEquals("Descripción actualizada", result.getDescription());
        assertEquals("Hamburguesa Clásica", result.getName());
        assertEquals(1L, result.getRestaurantId());
        assertEquals(1L, result.getCategoryId());
    }

    @Test
    void update_whenUserIsNotOwner_shouldThrowUserIsNotOwnerException() {
        when(dishPersistencePort.findById(1L)).thenReturn(buildExistingDish());
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> dishUseCase.update(1L, new Dish()));

        verify(dishPersistencePort, never()).update(any());
    }

    @Test
    void update_whenUserIsNotOwner_shouldNotPersistChanges() {
        when(dishPersistencePort.findById(1L)).thenReturn(buildExistingDish());
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> dishUseCase.update(1L, new Dish()));

        verifyNoMoreInteractions(dishPersistencePort);
    }

    @Test
    void update_shouldFetchDishWithCorrectId() {
        when(dishPersistencePort.findById(1L)).thenReturn(buildExistingDish());
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.update(any())).thenReturn(buildExistingDish());

        dishUseCase.update(1L, new Dish());

        verify(dishPersistencePort).findById(1L);
    }

    @Test
    void update_shouldValidateOwnershipUsingRestaurantFromExistingDish() {
        Dish existing = buildExistingDish();
        when(dishPersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.update(any())).thenReturn(existing);

        dishUseCase.update(1L, new Dish());

        verify(restaurantPersistencePort).findById(existing.getRestaurantId());
    }

    @Test
    void toggleActive_whenDishIsActiveAndUserIsOwner_shouldSetActiveFalse() {
        Dish existing = buildExistingDish();
        when(dishPersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.update(existing)).thenReturn(existing);

        Dish result = dishUseCase.toggleActive(1L);

        assertFalse(result.getActive());
        verify(dishPersistencePort).update(existing);
    }

    @Test
    void toggleActive_whenDishIsInactiveAndUserIsOwner_shouldSetActiveTrue() {
        Dish existing = buildExistingDish();
        existing.setActive(false);
        when(dishPersistencePort.findById(1L)).thenReturn(existing);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.update(existing)).thenReturn(existing);

        Dish result = dishUseCase.toggleActive(1L);

        assertTrue(result.getActive());
        verify(dishPersistencePort).update(existing);
    }

    @Test
    void toggleActive_whenUserIsNotOwner_shouldThrowUserIsNotOwnerException() {
        when(dishPersistencePort.findById(1L)).thenReturn(buildExistingDish());
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> dishUseCase.toggleActive(1L));

        verify(dishPersistencePort, never()).update(any());
    }

    @Test
    void toggleActive_whenUserIsNotOwner_shouldNotPersistChanges() {
        when(dishPersistencePort.findById(1L)).thenReturn(buildExistingDish());
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(99L);

        assertThrows(UserIsNotOwnerException.class,
                () -> dishUseCase.toggleActive(1L));

        verifyNoMoreInteractions(dishPersistencePort);
    }

    @Test
    void toggleActive_shouldFetchDishWithCorrectId() {
        when(dishPersistencePort.findById(1L)).thenReturn(buildExistingDish());
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(10L);
        when(dishPersistencePort.update(any())).thenReturn(buildExistingDish());

        dishUseCase.toggleActive(1L);

        verify(dishPersistencePort).findById(1L);
    }

    private Dish buildExistingDish() {
        Dish existing = new Dish();
        existing.setId(1L);
        existing.setName("Hamburguesa Clásica");
        existing.setPrice(25000);
        existing.setDescription("Descripción original");
        existing.setUrlImage("https://storage.com/hamburguesa.png");
        existing.setCategoryId(1L);
        existing.setRestaurantId(1L);
        existing.setActive(true);
        return existing;
    }
}
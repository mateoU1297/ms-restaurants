package com.pragma.restaurants.domain.usecase;

import com.pragma.restaurants.domain.exception.ClientHasActiveOrderException;
import com.pragma.restaurants.domain.exception.DishNotAvailableException;
import com.pragma.restaurants.domain.exception.DishNotBelongsToRestaurantException;
import com.pragma.restaurants.domain.exception.InvalidSecurityPinException;
import com.pragma.restaurants.domain.exception.OrderNotFromRestaurantException;
import com.pragma.restaurants.domain.exception.OrderNotInPreparationException;
import com.pragma.restaurants.domain.exception.OrderNotPendingException;
import com.pragma.restaurants.domain.exception.OrderNotReadyException;
import com.pragma.restaurants.domain.model.Dish;
import com.pragma.restaurants.domain.model.Order;
import com.pragma.restaurants.domain.model.OrderDish;
import com.pragma.restaurants.domain.model.Page;
import com.pragma.restaurants.domain.model.Restaurant;
import com.pragma.restaurants.domain.model.enums.OrderStatus;
import com.pragma.restaurants.domain.model.events.OrderReadyEvent;
import com.pragma.restaurants.domain.spi.IDishPersistencePort;
import com.pragma.restaurants.domain.spi.IOrderEventPort;
import com.pragma.restaurants.domain.spi.IOrderPersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.restaurants.domain.spi.IRestaurantPersistencePort;
import com.pragma.restaurants.domain.spi.ISecurityContextPort;
import com.pragma.restaurants.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private IDishPersistencePort dishPersistencePort;
    @Mock
    private ISecurityContextPort securityContextPort;
    @Mock
    private IRestaurantEmployeePersistencePort restaurantEmployeePersistencePort;
    @Mock
    private IOrderEventPort orderEventPort;
    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private Order order;
    private Restaurant restaurant;
    private Dish dish;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setOwnerId(10L);

        dish = new Dish();
        dish.setId(1L);
        dish.setRestaurantId(1L);
        dish.setActive(true);

        OrderDish orderDish = new OrderDish();
        orderDish.setDishId(1L);
        orderDish.setQuantity(2);

        order = new Order();
        order.setRestaurantId(1L);
        order.setDishes(List.of(orderDish));
    }

    @Test
    void save_whenValidOrder_shouldSetClientIdAndStatusPendingAndSave() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(false);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.findById(1L)).thenReturn(dish);
        when(orderPersistencePort.save(any())).thenReturn(order);

        Order result = orderUseCase.save(order);

        assertNotNull(result);
        assertEquals(5L, order.getClientId());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        verify(orderPersistencePort).save(order);
    }

    @Test
    void save_whenClientHasActiveOrder_shouldThrowClientHasActiveOrderException() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(true);

        assertThrows(ClientHasActiveOrderException.class,
                () -> orderUseCase.save(order));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void save_whenClientHasActiveOrder_shouldNotValidateDishesNorRestaurant() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(true);

        assertThrows(ClientHasActiveOrderException.class,
                () -> orderUseCase.save(order));

        verifyNoInteractions(restaurantPersistencePort);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void save_whenDishBelongsToDifferentRestaurant_shouldThrowDishNotBelongsToRestaurantException() {
        dish.setRestaurantId(99L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(false);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.findById(1L)).thenReturn(dish);

        assertThrows(DishNotBelongsToRestaurantException.class,
                () -> orderUseCase.save(order));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void save_whenDishIsNotActive_shouldThrowDishNotAvailableException() {
        dish.setActive(false);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(false);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.findById(1L)).thenReturn(dish);

        assertThrows(DishNotAvailableException.class,
                () -> orderUseCase.save(order));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void save_shouldGetClientIdFromSecurityContext() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(false);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.findById(1L)).thenReturn(dish);
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.save(order);

        verify(securityContextPort).getAuthenticatedUserId();
    }

    @Test
    void save_shouldVerifyRestaurantExists() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(false);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.findById(1L)).thenReturn(dish);
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.save(order);

        verify(restaurantPersistencePort).findById(1L);
    }

    @Test
    void save_shouldValidateEachDishInOrder() {
        OrderDish orderDish2 = new OrderDish();
        orderDish2.setDishId(2L);
        orderDish2.setQuantity(1);

        Dish dish2 = new Dish();
        dish2.setId(2L);
        dish2.setRestaurantId(1L);
        dish2.setActive(true);

        order.setDishes(List.of(order.getDishes().get(0), orderDish2));

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(5L);
        when(orderPersistencePort.hasActiveOrder(5L)).thenReturn(false);
        when(restaurantPersistencePort.findById(1L)).thenReturn(restaurant);
        when(dishPersistencePort.findById(1L)).thenReturn(dish);
        when(dishPersistencePort.findById(2L)).thenReturn(dish2);
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.save(order);

        verify(dishPersistencePort).findById(1L);
        verify(dishPersistencePort).findById(2L);
    }

    @Test
    void findByRestaurantAndStatus_shouldReturnPagedOrdersFromEmployeeRestaurant() {
        List<Order> orders = List.of(order);
        Page<Order> expectedPage = new Page<>(orders, 0, 10, 1L, 1, true);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantAndStatus(1L, OrderStatus.PENDING, 0, 10))
                .thenReturn(expectedPage);

        Page<Order> result = orderUseCase.findByRestaurantAndStatus(OrderStatus.PENDING, 0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    void findByRestaurantAndStatus_shouldGetEmployeeIdFromSecurityContext() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantAndStatus(any(), any(), anyInt(), anyInt()))
                .thenReturn(new Page<>(List.of(), 0, 10, 0L, 0, true));

        orderUseCase.findByRestaurantAndStatus(OrderStatus.PENDING, 0, 10);

        verify(securityContextPort).getAuthenticatedUserId();
    }

    @Test
    void findByRestaurantAndStatus_shouldFetchRestaurantFromEmployeeAssignment() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantAndStatus(any(), any(), anyInt(), anyInt()))
                .thenReturn(new Page<>(List.of(), 0, 10, 0L, 0, true));

        orderUseCase.findByRestaurantAndStatus(OrderStatus.PENDING, 0, 10);

        verify(restaurantEmployeePersistencePort).findRestaurantIdByEmployeeId(20L);
    }

    @Test
    void findByRestaurantAndStatus_shouldPassCorrectRestaurantIdToOrderPort() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantAndStatus(1L, OrderStatus.IN_PREPARATION, 1, 5))
                .thenReturn(new Page<>(List.of(), 1, 5, 0L, 0, true));

        orderUseCase.findByRestaurantAndStatus(OrderStatus.IN_PREPARATION, 1, 5);

        verify(orderPersistencePort).findByRestaurantAndStatus(1L, OrderStatus.IN_PREPARATION, 1, 5);
    }

    @Test
    void findByRestaurantAndStatus_shouldNotInteractWithRestaurantOrDishPort() {
        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findByRestaurantAndStatus(any(), any(), anyInt(), anyInt()))
                .thenReturn(new Page<>(List.of(), 0, 10, 0L, 0, true));

        orderUseCase.findByRestaurantAndStatus(OrderStatus.PENDING, 0, 10);

        verifyNoInteractions(restaurantPersistencePort);
        verifyNoInteractions(dishPersistencePort);
    }

    @Test
    void assignEmployee_whenOrderIsPendingAndFromRestaurant_shouldAssignEmployeeAndChangeStatus() {
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(1L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.save(order)).thenReturn(order);

        Order result = orderUseCase.assignEmployee(1L);

        assertNotNull(result);
        assertEquals(20L, order.getEmployeeId());
        assertEquals(OrderStatus.IN_PREPARATION, order.getStatus());
        verify(orderPersistencePort).save(order);
    }

    @Test
    void assignEmployee_whenOrderNotFromRestaurant_shouldThrowOrderNotFromRestaurantException() {
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(99L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotFromRestaurantException.class,
                () -> orderUseCase.assignEmployee(1L));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void assignEmployee_whenOrderIsNotPending_shouldThrowOrderNotPendingException() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(1L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotPendingException.class,
                () -> orderUseCase.assignEmployee(1L));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void assignEmployee_whenOrderIsDelivered_shouldThrowOrderNotPendingException() {
        order.setId(1L);
        order.setStatus(OrderStatus.DELIVERED);
        order.setRestaurantId(1L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotPendingException.class,
                () -> orderUseCase.assignEmployee(1L));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void assignEmployee_shouldGetEmployeeIdFromSecurityContext() {
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(1L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.assignEmployee(1L);

        verify(securityContextPort, times(2)).getAuthenticatedUserId();
    }

    @Test
    void assignEmployee_shouldFetchOrderWithCorrectId() {
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(1L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.assignEmployee(1L);

        verify(orderPersistencePort).findById(1L);
    }

    @Test
    void assignEmployee_shouldValidateRestaurantFromEmployeeAssignment() {
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(1L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.assignEmployee(1L);

        verify(restaurantEmployeePersistencePort).findRestaurantIdByEmployeeId(20L);
    }

    @Test
    void notifyOrderReady_whenOrderIsInPreparation_shouldSetReadyAndPublishEvent() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(1L);
        order.setClientId(5L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(userPersistencePort.getClientPhone(5L)).thenReturn("+573001234567");
        when(orderPersistencePort.save(order)).thenReturn(order);

        Order result = orderUseCase.notifyOrderReady(1L);

        assertEquals(OrderStatus.READY, order.getStatus());
        assertNotNull(order.getSecurityPin());
        assertEquals("+573001234567", order.getClientPhone());
        verify(orderPersistencePort).save(order);
        verify(orderEventPort).publishOrderReady(any(OrderReadyEvent.class));
    }

    @Test
    void notifyOrderReady_shouldGenerateSixDigitPin() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(1L);
        order.setClientId(5L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(userPersistencePort.getClientPhone(5L)).thenReturn("+573001234567");
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.notifyOrderReady(1L);

        assertNotNull(order.getSecurityPin());
        assertEquals(6, order.getSecurityPin().length());
        assertTrue(order.getSecurityPin().matches("\\d{6}"));
    }

    @Test
    void notifyOrderReady_shouldPublishEventWithCorrectData() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(1L);
        order.setClientId(5L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(userPersistencePort.getClientPhone(5L)).thenReturn("+573001234567");
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.notifyOrderReady(1L);

        verify(orderEventPort).publishOrderReady(argThat(event ->
                event.getOrderId().equals(1L) &&
                        event.getClientId().equals(5L) &&
                        event.getClientPhone().equals("+573001234567") &&
                        event.getSecurityPin() != null
        ));
    }

    @Test
    void notifyOrderReady_whenOrderIsNotInPreparation_shouldThrowOrderNotInPreparationException() {
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setRestaurantId(1L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotInPreparationException.class,
                () -> orderUseCase.notifyOrderReady(1L));

        verify(orderPersistencePort, never()).save(any());
        verifyNoInteractions(orderEventPort);
    }

    @Test
    void notifyOrderReady_whenOrderNotFromRestaurant_shouldThrowOrderNotFromRestaurantException() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(99L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotFromRestaurantException.class,
                () -> orderUseCase.notifyOrderReady(1L));

        verify(orderPersistencePort, never()).save(any());
        verifyNoInteractions(orderEventPort);
        verifyNoInteractions(userPersistencePort);
    }

    @Test
    void notifyOrderReady_whenOrderNotFromRestaurant_shouldNotFetchClientPhone() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(99L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotFromRestaurantException.class,
                () -> orderUseCase.notifyOrderReady(1L));

        verifyNoInteractions(userPersistencePort);
    }

    @Test
    void notifyOrderReady_shouldUpdateOrderBeforePublishingEvent() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(1L);
        order.setClientId(5L);

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(userPersistencePort.getClientPhone(5L)).thenReturn("+573001234567");
        when(orderPersistencePort.save(any())).thenReturn(order);

        orderUseCase.notifyOrderReady(1L);

        InOrder inOrder = inOrder(orderPersistencePort, orderEventPort);
        inOrder.verify(orderPersistencePort).save(any());
        inOrder.verify(orderEventPort).publishOrderReady(any());
    }

    @Test
    void deliverOrder_whenOrderIsReadyAndPinIsValid_shouldSetDeliveredStatus() {
        order.setId(1L);
        order.setStatus(OrderStatus.READY);
        order.setRestaurantId(1L);
        order.setSecurityPin("123456");

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);
        when(orderPersistencePort.save(order)).thenReturn(order);

        Order result = orderUseCase.deliverOrder(1L, "123456");

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        verify(orderPersistencePort).save(order);
    }

    @Test
    void deliverOrder_whenPinIsInvalid_shouldThrowInvalidSecurityPinException() {
        order.setId(1L);
        order.setStatus(OrderStatus.READY);
        order.setRestaurantId(1L);
        order.setSecurityPin("123456");

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(InvalidSecurityPinException.class,
                () -> orderUseCase.deliverOrder(1L, "999999"));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void deliverOrder_whenOrderIsNotReady_shouldThrowOrderNotReadyException() {
        order.setId(1L);
        order.setStatus(OrderStatus.IN_PREPARATION);
        order.setRestaurantId(1L);
        order.setSecurityPin("123456");

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotReadyException.class,
                () -> orderUseCase.deliverOrder(1L, "123456"));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void deliverOrder_whenOrderNotFromRestaurant_shouldThrowOrderNotFromRestaurantException() {
        order.setId(1L);
        order.setStatus(OrderStatus.READY);
        order.setRestaurantId(99L);
        order.setSecurityPin("123456");

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(OrderNotFromRestaurantException.class,
                () -> orderUseCase.deliverOrder(1L, "123456"));

        verify(orderPersistencePort, never()).save(any());
    }

    @Test
    void deliverOrder_whenPinIsInvalid_shouldNotPersistChanges() {
        order.setId(1L);
        order.setStatus(OrderStatus.READY);
        order.setRestaurantId(1L);
        order.setSecurityPin("123456");

        when(securityContextPort.getAuthenticatedUserId()).thenReturn(20L);
        when(restaurantEmployeePersistencePort.findRestaurantIdByEmployeeId(20L)).thenReturn(1L);
        when(orderPersistencePort.findById(1L)).thenReturn(order);

        assertThrows(InvalidSecurityPinException.class,
                () -> orderUseCase.deliverOrder(1L, "000000"));

        verifyNoMoreInteractions(orderPersistencePort);
    }

}
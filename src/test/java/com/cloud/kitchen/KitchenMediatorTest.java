package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.MatchedOrderDispatcherStrategy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class KitchenMediatorTest {

    @InjectMocks
    private KitchenMediator mediator;

    private ScheduledExecutorService testScheduler;

    private Order order;

    private Courier courier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mediator.setDispatchCommand(new MatchedOrderDispatcherStrategy());
        testScheduler = Executors.newSingleThreadScheduledExecutor();
        this.order = new Order("1", "Burger", 5,System.currentTimeMillis());
        this.courier = Courier.createCourier(23);
    }

    @AfterEach
    void tearDown() {
        testScheduler.shutdownNow();
    }

    @Test
    void testAddOrder() {
        mediator.addOrder(order);

        // Verify that the order was added to the mediator
        assertEquals(1, mediator.getOrders().size());
        assertEquals(order, mediator.getOrders().peek());
    }

    @Test
    void testAddDriver() {
        mediator.addCourier(courier);

        // Verify that the courier was added to the mediator
        assertEquals(1, mediator.getWaitingCouriers().size());
        assertEquals(courier, mediator.getWaitingCouriers().peek());
    }

    @Test
    void testDispatchOrder() throws InterruptedException {
        // Prepare mock objects
        mediator.addOrder(order);
        mediator.addCourier(courier);

        // Wait for the scheduled task to complete
        testScheduler.schedule(() ->
                mediator.dispatchOrder(), order.getPrepTime(), TimeUnit.SECONDS);

        testScheduler.awaitTermination(2, TimeUnit.SECONDS);

        // Verify that the order was dispatched correctly
        assertEquals(0, mediator.getReadyOrders().size());
        assertEquals(1, mediator.getWaitingCouriers().size());
    }

    @Test
    void testRegisterOrderReadyObserver() throws InterruptedException {
        OrderReadyObserver observer = mock(OrderReadyObserver.class);
        mediator.registerOrderReadyObserver(observer);

        mediator.addOrder(order);

        // Fast forward the timer to simulate order preparation completion
        testScheduler.schedule(() -> {
            order.setReadyTime(System.currentTimeMillis());
            mediator.getReadyOrders().add(order);
            mediator.notifyOrderReadyObservers(order);
        }, 1, TimeUnit.SECONDS);

        testScheduler.awaitTermination(2, TimeUnit.SECONDS);

        verify(observer, times(1)).onOrderReady(order);
    }

    @Test
    void testRegisterDriverArrivalObserver() {
        com.cloud.kitchen.observer.CourierArrivalObserver observer = mock(com.cloud.kitchen.observer.CourierArrivalObserver.class);
        mediator.registerCourierArrivalObserver(observer);

        mediator.addCourier(courier);

        // Verify that the observer's onDriverArrival method was called
        verify(observer, times(1)).updateCourierArrival(courier);
    }

    @Test
    void testNotifyOrderReadyObservers() {
        OrderReadyObserver observer = mock(OrderReadyObserver.class);
        mediator.registerOrderReadyObserver(observer);

        mediator.notifyOrderReadyObservers(order);

        // Verify that the observer's onOrderReady method was called
        verify(observer, times(1)).onOrderReady(order);
    }

    @Test
    void testNotifyDriverArrivalObservers() {
        com.cloud.kitchen.observer.CourierArrivalObserver observer = mock(com.cloud.kitchen.observer.CourierArrivalObserver.class);
        mediator.registerCourierArrivalObserver(observer);

        mediator.notifyDriverArrivalObservers(courier);

        // Verify that the observer's onDriverArrival method was called
        verify(observer, times(1)).updateCourierArrival(courier);
    }
}
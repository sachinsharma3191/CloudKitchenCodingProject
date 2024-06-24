package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Driver;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.observer.DriverArrivalObserver;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.MatchedOrderDispatcherStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mediator.setDispatchCommand(new MatchedOrderDispatcherStrategy());
        testScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    @AfterEach
    void tearDown() {
        testScheduler.shutdownNow();
        mediator.shutdown();
    }

    @Test
    void testAddOrder() {
        Order order = new Order("1", "Burger",5);
        mediator.addOrder(order);

        // Verify that the order was added to the mediator
        assertEquals(1, mediator.getOrders().size());
        assertEquals(order, mediator.getOrders().peek());
    }

    @Test
    void testAddDriver() {
        Driver driver = Driver.createDriver(23);
        mediator.addDriver(driver);

        // Verify that the driver was added to the mediator
        assertEquals(1, mediator.getWaitingDrivers().size());
        assertEquals(driver, mediator.getWaitingDrivers().peek());
    }

    @Test
    @Disabled
    void testDispatchOrder() throws InterruptedException {
        // Prepare mock objects
        Order order = new Order("1", "Burger", 5);
        Driver driver = Driver.createDriver(45);
        mediator.addOrder(order);
        mediator.addDriver(driver);

        // Wait for the scheduled task to complete
        TimeUnit.SECONDS.sleep(2);
        mediator.dispatchOrder();

        // Verify that the order was dispatched correctly
        assertEquals(0, mediator.getReadyOrders().size());
        assertEquals(0, mediator.getWaitingDrivers().size());
    }

    @Test
    void testRegisterOrderReadyObserver() throws InterruptedException {
        OrderReadyObserver observer = mock(OrderReadyObserver.class);
        mediator.registerOrderReadyObserver(observer);

        Order order = new Order("1", "Burger", 5);
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
        DriverArrivalObserver observer = mock(DriverArrivalObserver.class);
        mediator.registerDriverArrivalObserver(observer);

        Driver driver = Driver.createDriver(23);
        mediator.addDriver(driver);

        // Verify that the observer's onDriverArrival method was called
        verify(observer, times(1)).onDriverArrival(driver);
    }

    @Test
    void testNotifyOrderReadyObservers() {
        OrderReadyObserver observer = mock(OrderReadyObserver.class);
        mediator.registerOrderReadyObserver(observer);

        Order order = new Order("1", "Burger", 5);
        mediator.notifyOrderReadyObservers(order);

        // Verify that the observer's onOrderReady method was called
        verify(observer, times(1)).onOrderReady(order);
    }

    @Test
    void testNotifyDriverArrivalObservers() {
        DriverArrivalObserver observer = mock(DriverArrivalObserver.class);
        mediator.registerDriverArrivalObserver(observer);

        Driver driver = Driver.createDriver(23);
        mediator.notifyDriverArrivalObservers(driver);

        // Verify that the observer's onDriverArrival method was called
        verify(observer, times(1)).onDriverArrival(driver);
    }
}
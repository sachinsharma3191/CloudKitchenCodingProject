package com.cloud.kitchen;

import java.util.UUID;

import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.OrderDispatcherStrategy;
import com.cloud.kitchen.stragety.MatchedOrderDispatcherStrategy;
import com.cloud.kitchen.stragety.FifoOrderDispatcherStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cloud.kitchen.mediator.KitchenMediator;

/**
 * The KitchenMediatorTest class contains unit tests for the KitchenMediator class and its functionalities.
 * It tests order processing, courier dispatch strategies, observer notifications, and average calculations.
 */
class KitchenMediatorTest {

    private KitchenMediator kitchenMediator;
    private OrderDispatcherStrategy matchedStrategy;
    private OrderDispatcherStrategy fifoStrategy;
    private OrderReadyObserver orderReadyObserver;

    /**
     * Initializes the test environment before each test method runs.
     */
    @BeforeEach
    void setUp() {
        kitchenMediator = new KitchenMediator();
        matchedStrategy = new MatchedOrderDispatcherStrategy();
        fifoStrategy = new FifoOrderDispatcherStrategy();
        orderReadyObserver = Mockito.mock(OrderReadyObserver.class);
        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerCourierArrivalObserver(Mockito.mock(CourierArrivalObserver.class));
    }

    /**
     * Tests the addition of an order to the kitchen mediator.
     */
    @Test
    void testAddOrder() {
        Order order = new Order(UUID.randomUUID().toString(),"Burger", 5);
        kitchenMediator.addOrder(order);

        assertFalse(kitchenMediator.getOrders().isEmpty());
        assertTrue(kitchenMediator.getOrders().contains(order));
    }

    /**
     * Tests the preparation of an order and the notification of observers when it's ready.
     *
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    void testPrepareOrder() throws InterruptedException {
        Order order = new Order(UUID.randomUUID().toString(),"Burger", 4);
        kitchenMediator.addOrder(order);

        TimeUnit.SECONDS.sleep(2); // Wait for the order to be prepared

        assertFalse(kitchenMediator.getReadyOrders().isEmpty());
        assertTrue(kitchenMediator.getReadyOrders().contains(order));
        verify(orderReadyObserver, atLeastOnce()).onOrderReady(order);
    }

    /**
     * Tests the addition of a courier to the kitchen mediator.
     */
    @Test
    void testAddCourier() {
        Courier courier = new Courier(1);
        kitchenMediator.addCourier(courier);

        assertFalse(kitchenMediator.getWaitingCouriers().isEmpty());
        assertTrue(kitchenMediator.getWaitingCouriers().contains(courier));
    }

    /**
     * Tests order dispatch using the Matched Order Dispatcher Strategy.
     *
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    void testDispatchOrderMatchedStrategy() throws InterruptedException {
        kitchenMediator.setDispatchCommand(matchedStrategy);

        Order order = new Order(UUID.randomUUID().toString(),"Burger", 12);
        Courier courier = new Courier(1);

        kitchenMediator.addOrder(order);
        kitchenMediator.addCourier(courier);

        TimeUnit.SECONDS.sleep(2); // Wait for the order to be prepared

        assertTrue(kitchenMediator.getReadyOrders().isEmpty());
        assertTrue(kitchenMediator.getWaitingCouriers().isEmpty());
    }

    /**
     * Tests order dispatch using the FIFO Order Dispatcher Strategy.
     *
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    void testDispatchOrderFIFOStrategy() throws InterruptedException {
        kitchenMediator.setDispatchCommand(fifoStrategy);

        Order order1 = new Order(UUID.randomUUID().toString(),"Burger", 3);
        Order order2 = new Order(UUID.randomUUID().toString(),"Waffles", 1);
        Courier courier1 = new Courier(1);
        Courier courier2 = new Courier(2);

        kitchenMediator.addOrder(order1);
        kitchenMediator.addOrder(order2);
        kitchenMediator.addCourier(courier1);
        kitchenMediator.addCourier(courier2);

        TimeUnit.SECONDS.sleep(2); // Wait for the orders to be prepared

        assertTrue(kitchenMediator.getReadyOrders().isEmpty());
        assertTrue(kitchenMediator.getWaitingCouriers().isEmpty());
    }

    /**
     * Tests food wait time calculation after order pickup.
     *
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    void testFoodWaitTimeCalculation() throws InterruptedException {
        kitchenMediator.setDispatchCommand(fifoStrategy);

        Order order = new Order(UUID.randomUUID().toString(),"Burger", 5);
        Courier courier = new Courier(1);

        kitchenMediator.addOrder(order);
        kitchenMediator.addCourier(courier);

        TimeUnit.SECONDS.sleep(8); // Wait for the order to be prepared

        double foodWaitTime = kitchenMediator.getFoodWaitTimes().getFirst();
        assertTrue(foodWaitTime > 0);
    }

    /**
     * Tests courier wait time calculation after order pickup.
     *
     * @throws InterruptedException if the thread is interrupted during sleep.
     */
    @Test
    void testCourierWaitTimeCalculation() throws InterruptedException {
        kitchenMediator.setDispatchCommand(fifoStrategy);

        Order order = new Order(UUID.randomUUID().toString(),"Burger", 6);
        Courier courier = new Courier(1);

        kitchenMediator.addOrder(order);
        kitchenMediator.addCourier(courier);

        TimeUnit.SECONDS.sleep(2); // Wait for the order to be prepared

        double courierWaitTime = kitchenMediator.getCourierWaitTimes().getFirst();
        assertTrue(courierWaitTime >= 0);
    }

    /**
     * Tests printing of average statistics for food and courier wait times.
     */
    @Test
    void testPrintAverages() {
        kitchenMediator.setDispatchCommand(fifoStrategy);

        Order order1 = new Order(UUID.randomUUID().toString(),"Burger", 3);
        Order order2 = new Order(UUID.randomUUID().toString(),"Waffles", 1);
        Courier courier1 = new Courier(1);
        Courier courier2 = new Courier(2);

        kitchenMediator.addOrder(order1);
        kitchenMediator.addOrder(order2);
        kitchenMediator.addCourier(courier1);
        kitchenMediator.addCourier(courier2);

        kitchenMediator.printAverages();

        assertNotNull(kitchenMediator.getFoodWaitTimes());
        assertNotNull(kitchenMediator.getCourierWaitTimes());
    }
}

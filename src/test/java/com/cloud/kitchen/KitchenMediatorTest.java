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

class KitchenMediatorTest {

    private KitchenMediator kitchenMediator;
    private OrderDispatcherStrategy matchedStrategy;
    private OrderDispatcherStrategy fifoStrategy;
    private OrderReadyObserver orderReadyObserver;
    private CourierArrivalObserver courierArrivalObserver;

    @BeforeEach
    void setUp() {
        kitchenMediator = new KitchenMediator();
        matchedStrategy = new MatchedOrderDispatcherStrategy();
        fifoStrategy = new FifoOrderDispatcherStrategy();
        orderReadyObserver = Mockito.mock(OrderReadyObserver.class);
        courierArrivalObserver = Mockito.mock(CourierArrivalObserver.class);

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerCourierArrivalObserver(courierArrivalObserver);
    }

    @Test
    void testAddOrder() {
        Order order = new Order(UUID.randomUUID().toString(),"Burger", 5);
        kitchenMediator.addOrder(order);

        assertFalse(kitchenMediator.getOrders().isEmpty());
        assertTrue(kitchenMediator.getOrders().contains(order));
    }

    @Test
    void testPrepareOrder() throws InterruptedException {
        Order order = new Order(UUID.randomUUID().toString(),"Burger", 4);
        kitchenMediator.addOrder(order);

        TimeUnit.SECONDS.sleep(2); // Wait for the order to be prepared

        assertFalse(kitchenMediator.getReadyOrders().isEmpty());
        assertTrue(kitchenMediator.getReadyOrders().contains(order));
        verify(orderReadyObserver, atLeastOnce()).onOrderReady(order);
    }

    @Test
    void testAddCourier() {
        Courier courier = new Courier(1);
        kitchenMediator.addCourier(courier);

        assertFalse(kitchenMediator.getWaitingCouriers().isEmpty());
        assertTrue(kitchenMediator.getWaitingCouriers().contains(courier));
    }

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

    @Test
    void testFoodWaitTimeCalculation() throws InterruptedException {
        kitchenMediator.setDispatchCommand(fifoStrategy);

        Order order = new Order(UUID.randomUUID().toString(),"Burger", 5);
        Courier courier = new Courier(1);

        kitchenMediator.addOrder(order);
        kitchenMediator.addCourier(courier);

        TimeUnit.SECONDS.sleep(2); // Wait for the order to be prepared

        double foodWaitTime = kitchenMediator.getFoodWaitTimes().get(0);
        assertTrue(foodWaitTime > 0);
    }

    @Test
    void testCourierWaitTimeCalculation() throws InterruptedException {
        kitchenMediator.setDispatchCommand(fifoStrategy);

        Order order = new Order(UUID.randomUUID().toString(),"Burger", 6);
        Courier courier = new Courier(1);

        kitchenMediator.addOrder(order);
        kitchenMediator.addCourier(courier);

        TimeUnit.SECONDS.sleep(2); // Wait for the order to be prepared

        double courierWaitTime = kitchenMediator.getCourierWaitTimes().get(0);
        assertTrue(courierWaitTime >= 0);
    }

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

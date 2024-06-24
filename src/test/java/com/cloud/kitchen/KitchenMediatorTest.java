package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Driver;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.stragety.MatchedOrderDispatcherStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KitchenMediatorTest {

    @InjectMocks
    private KitchenMediator mediator;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mediator.setDispatchCommand(new MatchedOrderDispatcherStrategy());
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
    void testDispatchOrder() {
        // Prepare mock objects
        Order order = new Order("1", "Burger", 5);
        Driver driver = Driver.createDriver(45);
        mediator.addOrder(order);
        mediator.addDriver(driver);

        mediator.dispatchOrder();

        // Verify that the order was dispatched correctly
        assertEquals(0, mediator.getReadyOrders().size());
        assertEquals(0, mediator.getWaitingDrivers().size());
    }

    // Additional test cases can be added for observer registration and notification

}

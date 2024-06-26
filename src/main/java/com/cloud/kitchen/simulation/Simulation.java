package com.cloud.kitchen.simulation;

import com.cloud.kitchen.factory.CourierFactory;
import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.util.JsonUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.cloud.kitchen.util.Utility.currentMilliSeconds;

public class Simulation {

    private final static Logger logger = LogManager.getLogger(Simulation.class);

    private final List<Order> orders;
    private final KitchenMediator kitchenMediator;
    private final ScheduledExecutorService orderExecutorService;
    private final ScheduledExecutorService courierExecutorService;

    public Simulation(KitchenMediator kitchenMediator) {
        this.orders = getOrders();
        this.kitchenMediator = kitchenMediator;
        orderExecutorService = getScheduledExecutorService();
        courierExecutorService = getScheduledExecutorService();
    }

    private List<Order> getOrders() {
        List<Order> orders = JsonUtility.readOrders();
        if(CollectionUtils.isEmpty(orders)) {
            logger.error("Empty Orders");
            System.exit(1);
        }
        logger.info("Processing {} Orders", orders.size());
        return orders;
    }

    /**
     * Processing Orders
     */
    public void processOrders() {
        simulateOrdersSubmission();
        simulateDriverSubmission();
    }

    /**
     * Simulating Courier added for Order Delivery
     */
    private void simulateDriverSubmission() {
        courierExecutorService.scheduleAtFixedRate(() -> kitchenMediator.addCourier(CourierFactory.createCourier()), 0, 4, TimeUnit.SECONDS); // Add a new driver every 4 seconds
    }

    private void simulateOrdersSubmission() {
        for (Order order : orders) {
            orderExecutorService.schedule(() -> {
                order.setReadyTime(currentMilliSeconds());
                kitchenMediator.addOrder(order);
            }, order.getPrepTime(), TimeUnit.SECONDS);
        }
    }

    private ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }


    public void shutdown(){
        orderExecutorService.shutdownNow();
        courierExecutorService.shutdownNow();
    }
}

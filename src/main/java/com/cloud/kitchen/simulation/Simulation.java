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

/**
 * Simulation class manages the simulation of order processing and courier arrivals in a kitchen delivery system.
 */
public class Simulation {

    private final static Logger logger = LogManager.getLogger(Simulation.class);

    private final List<Order> orders;
    private final KitchenMediator kitchenMediator;
    private final ScheduledExecutorService orderExecutorService;
    private final ScheduledExecutorService courierExecutorService;

    /**
     * Constructor for Simulation class.
     *
     * @param kitchenMediator The mediator that manages orders and couriers in the simulation.
     */
    public Simulation(KitchenMediator kitchenMediator) {
        this.orders = getOrders();
        this.kitchenMediator = kitchenMediator;
        orderExecutorService = getScheduledExecutorService();
        courierExecutorService = getScheduledExecutorService();
    }

    /**
     * Retrieves the list of orders from JSON using a utility method.
     *
     * @return The list of orders read from JSON.
     */
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
     * Initiates the simulation by scheduling orders and courier arrivals.
     */
    public void processOrders() {
        simulateOrdersSubmission();
        simulateCourierArrivals();
    }

    /**
     * Schedules periodic courier arrivals based on a fixed interval.
     */
    private void simulateCourierArrivals() {
        courierExecutorService.scheduleAtFixedRate(() -> kitchenMediator.addCourier(CourierFactory.createCourier()), 0, 4, TimeUnit.SECONDS); // Add a new driver every 4 seconds
    }

    /**
     * Schedules orders for submission to the kitchen mediator based on their preparation times.
     */
    private void simulateOrdersSubmission() {
        for (Order order : orders) {
            orderExecutorService.schedule(() -> {
                order.setReadyTime(currentMilliSeconds());
                kitchenMediator.addOrder(order);
            }, order.getPrepTime(), TimeUnit.SECONDS);
        }
    }

    /**
     * Creates a new scheduled executor service with a fixed thread pool size.
     *
     * @return A new scheduled executor service.
     */
    private ScheduledExecutorService getScheduledExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }

    /**
     * Shuts down the order and courier executor services to end the simulation.
     */
    public void shutdown(){
        orderExecutorService.shutdownNow();
        courierExecutorService.shutdownNow();
    }
}

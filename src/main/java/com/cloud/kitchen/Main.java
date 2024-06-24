package com.cloud.kitchen;

import com.cloud.kitchen.factory.DriverFactory;
import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.observer.DriverArrivalObserver;
import com.cloud.kitchen.util.ExecutorServiceUtility;
import com.cloud.kitchen.util.JsonUtility;
import com.cloud.kitchen.util.Utility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import  com.cloud.kitchen.stragety.MatchedOrderDispatcherStrategy;

import static com.cloud.kitchen.util.Utility.decimalPrecision;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        KitchenMediator kitchenMediator = getKitchenMediator();
        List<Order> orders = JsonUtility.readOrders();
        if(CollectionUtils.isEmpty(orders)) {
            logger.error("Empty Orders");
            System.exit(1);
        }
        logger.info("Processing {} Orders", orders.size());
        processOrders(orders, kitchenMediator);
        kitchenMediator.setDispatchCommand(new MatchedOrderDispatcherStrategy());
        kitchenMediator.printAverages();
    }

    /**
     * Processing Orders
     *
     * @param orders List of Orders
     * @param kitchenMediator Object of KitchenMediator Class Processing Orders
     */
    private static void processOrders(List<Order> orders, KitchenMediator kitchenMediator) {
        ExecutorServiceUtility executorServiceUtility = new ExecutorServiceUtility();
        ScheduledExecutorService orderExecutor = executorServiceUtility.getScheduledExecutorService();
        ScheduledExecutorService driverExecutor = executorServiceUtility.getScheduledExecutorService();
        try {
            simulateOrdersSubmission(orders, kitchenMediator, orderExecutor);
            simulateDriverSubmission(kitchenMediator, driverExecutor);
        } finally {
            executorServiceUtility.shutDownExecutor(orderExecutor);
            executorServiceUtility.shutDownExecutor(driverExecutor);
        }
    }

    /**
     * Simulating Driver added for Order Delivery
     *
     * @param kitchenMediator Object of KitchenMediator Class Processing Orders
     * @param driverExecutorService Executor Simulating Driver added for Order Delivery
     */
    private static void simulateDriverSubmission(KitchenMediator kitchenMediator, ScheduledExecutorService driverExecutorService) {
        driverExecutorService.scheduleAtFixedRate(() -> kitchenMediator.addDriver(DriverFactory.createCourier()), 0, 4, TimeUnit.SECONDS); // Add a new driver every 4 seconds
    }

    /**
     * Simulating Order Submission/Processing
     *
     * @param orders List of Orders
     * @param kitchenMediator Object of KitchenMediator Class Processing Orders
     * @param orderExecutor Executor Simulating Order Submission/Processing
     */
    private static void simulateOrdersSubmission(List<Order> orders, KitchenMediator kitchenMediator, ScheduledExecutorService orderExecutor) {
        for (Order order : orders) {
            orderExecutor.schedule(() -> {
                order.setReadyTime(System.currentTimeMillis());
                kitchenMediator.addOrder(order);
            }, order.getPrepTime(), java.util.concurrent.TimeUnit.SECONDS);
        }
    }

    /**
     * @return Object of KitchenMediator Class Processing Orders
     */
    private static KitchenMediator getKitchenMediator() {
        KitchenMediator kitchenMediator = new KitchenMediator();

        // Register observers for order readiness and driver arrival
        OrderReadyObserver orderReadyObserver = order -> {
            double foodWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - order.getReadyTime());
            kitchenMediator.getFoodWaitTimes().add(foodWaitTime);
            logger.info("Order {} is ready.Food wait time: {} minutes", order.getId(), decimalPrecision(foodWaitTime));
        };

        DriverArrivalObserver driverArrivalObserver = driver -> {
            double driverWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - driver.getArrivalTime());
            kitchenMediator.getDriverWaitTimes().add(driverWaitTime);
            logger.info("Driver {} has arrived.Driver wait time: {} minutes.", driver.getDriverId(), decimalPrecision(driverWaitTime));
        };

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerDriverArrivalObserver(driverArrivalObserver);

        return kitchenMediator;
    }
}

package com.cloud.kitchen;

import com.cloud.kitchen.factory.DriverFactory;
import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.MatchedCourierDispatcherStrategy;
import com.cloud.kitchen.util.ExecutorServiceUtility;
import com.cloud.kitchen.util.JsonUtility;
import com.cloud.kitchen.util.Utility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

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
        kitchenMediator.setDispatchCommand(new MatchedCourierDispatcherStrategy());
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
        ScheduledExecutorService courierExecutor = executorServiceUtility.getScheduledExecutorService();
        try {
            simulateOrdersSubmission(orders, kitchenMediator, orderExecutor);
            simulateOrderDelivery(kitchenMediator, courierExecutor);
        } finally {
            executorServiceUtility.shutDownExecutor(orderExecutor);
            executorServiceUtility.shutDownExecutor(courierExecutor);
        }
    }

    /**
     * Simulating Driver added for Order Delivery
     *
     * @param kitchenMediator Object of KitchenMediator Class Processing Orders
     * @param courierExecutorService Executor Simulating Driver added for Order Delivery
     */
    private static void simulateOrderDelivery(KitchenMediator kitchenMediator, ScheduledExecutorService courierExecutorService) {
        courierExecutorService.scheduleAtFixedRate(() -> kitchenMediator.addDriver(DriverFactory.createCourier()), 0, 4, java.util.concurrent.TimeUnit.SECONDS); // Add a new courier every 4 seconds
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

        // Register observers for order readiness and courier arrival
        OrderReadyObserver orderReadyObserver = order -> {
            double foodWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - order.getReadyTime());
            kitchenMediator.getFoodWaitTimes().add(foodWaitTime);
            logger.info("Order {} is ready.Food wait time: {} minutes", order.getId(), foodWaitTime);
        };

        CourierArrivalObserver courierArrivalObserver = courier -> {
            double courierWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - courier.getArrivalTime());
            kitchenMediator.getCourierWaitTimes().add(courierWaitTime);
            logger.info("Driver {} has arrived.Driver wait time: {} minutes.", courier.getDriverId(), courierWaitTime);
        };

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerCourierArrivalObserver(courierArrivalObserver);

        return kitchenMediator;
    }
}

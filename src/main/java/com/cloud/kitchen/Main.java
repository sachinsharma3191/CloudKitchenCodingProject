package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Courier;
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
import java.util.concurrent.TimeUnit;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        List<Order> orders = JsonUtility.readOrders();
        logger.info("Processing {}", orders.size());
        if(CollectionUtils.isEmpty(orders)) {
            logger.error("Empty Orders");
            System.exit(1);
        }
        logger.info("Processing {}", orders.size());
        processOrders(orders);
    }

    private static void processOrders(List<Order> orders) {
        KitchenMediator kitchenMediator = getKitchenMediator();
        ExecutorServiceUtility executorServiceUtility = new ExecutorServiceUtility();

        ScheduledExecutorService orderExecutor = executorServiceUtility.getScheduledExecutorService();
        ScheduledExecutorService courierExecutor = executorServiceUtility.getScheduledExecutorService();
        try {
            for (Order order : orders) {
                orderExecutor.schedule(() -> {
                    order.setReadyTime(System.currentTimeMillis());
                    kitchenMediator.addOrder(order);
                }, order.getPrepTime(), TimeUnit.SECONDS);
            }

            courierExecutor.scheduleAtFixedRate(() -> {
                Courier courier = new Courier();
                kitchenMediator.addCourier(courier);
            }, 0, 4, TimeUnit.SECONDS); // Add a new courier every 4 seconds

            kitchenMediator.printAverages();
        } finally {
           orderExecutor.shutdown();
           courierExecutor.shutdown();
        }
    }

    private static KitchenMediator getKitchenMediator() {
        KitchenMediator kitchenMediator = new KitchenMediator();
        kitchenMediator.setDispatchCommand(new MatchedCourierDispatcherStrategy());

        // Register observers for order readiness and courier arrival
        OrderReadyObserver orderReadyObserver = order -> {
            long foodWaitTime = Utility.convertMillSecondToMinutes(System.currentTimeMillis() - order.getReadyTime());
            kitchenMediator.getFoodWaitTimes().add(foodWaitTime);
            logger.info("Order {} is ready.Food wait time: {}", order.getId(), foodWaitTime);
        };

        CourierArrivalObserver courierArrivalObserver = courier -> {
            long courierWaitTime = Utility.convertMillSecondToMinutes(System.currentTimeMillis() - courier.getArrivalTime());
            kitchenMediator.getCourierWaitTimes().add(courierWaitTime);
            logger.info("Courier {} has arrived.Courier wait time: {} minutes.", courier.getCourierId(), courierWaitTime);
        };

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerCourierArrivalObserver(courierArrivalObserver);

        return kitchenMediator;
    }
}

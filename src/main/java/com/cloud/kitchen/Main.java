package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.MatchedCourierDispatcherStrategy;
import com.cloud.kitchen.util.ExecutorServiceUtility;
import com.cloud.kitchen.util.JsonUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.cloud.kitchen.util.Utility;
import com.cloud.kitchen.factory.CourierFactory;

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
        processOrders(orders,kitchenMediator);
        kitchenMediator.printAverages();

    }

    private static void processOrders(List<Order> orders,KitchenMediator kitchenMediator) {
        ExecutorServiceUtility executorServiceUtility = new ExecutorServiceUtility();
        ScheduledExecutorService orderExecutor = executorServiceUtility.getScheduledExecutorService();
        ScheduledExecutorService courierExecutor = executorServiceUtility.getScheduledExecutorService();
        try {
            for (Order order : orders) {
                orderExecutor.schedule(() -> {
                    order.setReadyTime(System.currentTimeMillis());
                    kitchenMediator.addOrder(order);
                }, order.getPrepTime() , TimeUnit.SECONDS);
            }
            courierExecutor.scheduleAtFixedRate(() -> kitchenMediator.addCourier(CourierFactory.createCourier()), 0, 4, TimeUnit.SECONDS); // Add a new courier every 4 seconds
        } finally {
           executorServiceUtility.shutDownExecutor(orderExecutor);
           executorServiceUtility.shutDownExecutor(courierExecutor);
        }
    }

    private static KitchenMediator getKitchenMediator() {
        KitchenMediator kitchenMediator = new KitchenMediator();
        kitchenMediator.setDispatchCommand(new MatchedCourierDispatcherStrategy());

        // Register observers for order readiness and courier arrival
        OrderReadyObserver orderReadyObserver = order -> {
            double foodWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - order.getReadyTime());
            kitchenMediator.getFoodWaitTimes().add(foodWaitTime);
            logger.info("Order {} is ready.Food wait time: {} minutes", order.getId(), foodWaitTime);
        };

        CourierArrivalObserver courierArrivalObserver = courier -> {
            double courierWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - courier.getArrivalTime());
            kitchenMediator.getCourierWaitTimes().add(courierWaitTime);
            logger.info("Courier {} has arrived.Courier wait time: {} minutes.", courier.getCourierId(), courierWaitTime);
        };

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerCourierArrivalObserver(courierArrivalObserver);

        return kitchenMediator;
    }
}

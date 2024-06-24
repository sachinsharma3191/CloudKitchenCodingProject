package com.cloud.kitchen;

import java.util.List;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.util.JsonUtility;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.MatchedCourierDispatcherStrategy;
import com.cloud.kitchen.util.Utility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.TimeUnit;
import java.util.UUID;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        KitchenMediator kitchenMediator = getKitchenMediator();

        // Executor service for simulating orders and couriers
        List<Order> orders = JsonUtility.readOrders();
        if(CollectionUtils.isEmpty(orders)) {
            logger.error("Empty Orders");
            System.exit(1);
        }
        logger.info("Processing {}", orders.size());
        ExecutorService orderExecutor = Executors.newFixedThreadPool(2);
        ExecutorService courierExecutor = Executors.newFixedThreadPool(2);

        for (Order order : orders) {
            orderExecutor.submit(() -> {
                try {
                    Thread.sleep(order.getPrepTime() * 1000);
                    order.setReadyTime(System.currentTimeMillis());
                    kitchenMediator.addOrder(order);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        courierExecutor.submit(() -> simulateCouriers(kitchenMediator));
        courierExecutor.submit(() -> simulateCouriers(kitchenMediator));

        orderExecutor.shutdown();
        courierExecutor.shutdown();
        try {
            if (!orderExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                orderExecutor.shutdownNow();
            }
            if (!courierExecutor.awaitTermination(60, TimeUnit.SECONDS)) {
                courierExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            orderExecutor.shutdownNow();
            courierExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        kitchenMediator.printAverages();
    }

    private static com.cloud.kitchen.mediator.KitchenMediator getKitchenMediator() {
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


    // Method to simulate courier arrivals
    private static void simulateCouriers(KitchenMediator kitchen) {
        while (true) {
            Courier courier = new Courier();
            try {
                Thread.sleep((long) (Math.random() * 12000 + 3000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            kitchen.addCourier(courier);
        }
    }
}

package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.observer.DriverArrivalObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import  com.cloud.kitchen.stragety.FifoOrderDispatcherStrategy;


import static com.cloud.kitchen.util.Utility.convertToMinutes;
import static com.cloud.kitchen.util.Utility.currentLocalDateTime;
import static com.cloud.kitchen.util.Utility.decimalPrecision;

import com.cloud.kitchen.simulation.Simulation;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    /**
     * @return Object of KitchenMediator Class Processing Orders
     */
    private static KitchenMediator getKitchenMediator() {
        KitchenMediator kitchenMediator = new KitchenMediator();

        // Register observers for order readiness and driver arrival
        OrderReadyObserver orderReadyObserver = order -> {
            double foodWaitTime = convertToMinutes(order.getReadyTime(), currentLocalDateTime());
            kitchenMediator.getFoodWaitTimes().add(foodWaitTime);
            logger.info("Order {} is ready.Food wait time: {} minutes", order.getId(), decimalPrecision(foodWaitTime));
        };

        DriverArrivalObserver driverArrivalObserver = driver -> {
            double driverWaitTime = convertToMinutes(driver.getArrivalTime(), currentLocalDateTime());
            kitchenMediator.getDriverWaitTimes().add(driverWaitTime);
            logger.info("Courier {} has arrived.Courier wait time: {} minutes.", driver.getDriverId(), decimalPrecision(driverWaitTime));
        };

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerDriverArrivalObserver(driverArrivalObserver);

        return kitchenMediator;
    }

    public static void main(String[] args) {
        KitchenMediator kitchenMediator = getKitchenMediator();
        kitchenMediator.setDispatchCommand(new FifoOrderDispatcherStrategy());

        Simulation simulation = new Simulation(kitchenMediator);
    }

}

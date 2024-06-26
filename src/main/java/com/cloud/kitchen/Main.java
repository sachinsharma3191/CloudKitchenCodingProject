package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.simulation.Simulation;

import com.cloud.kitchen.stragety.MatchedOrderDispatcherStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.cloud.kitchen.util.Utility.convertToMinutes;
import static com.cloud.kitchen.util.Utility.currentMilliSeconds;
import static com.cloud.kitchen.util.Utility.decimalPrecision;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    /**
     * @return Object of KitchenMediator Class Processing Orders
     */
    private static KitchenMediator getKitchenMediator() {
        KitchenMediator kitchenMediator = new KitchenMediator();

        // Register observers for order readiness and driver arrival
        OrderReadyObserver orderReadyObserver = order -> {
            double foodWaitTime = convertToMinutes(currentMilliSeconds() - order.getReadyTime());
            kitchenMediator.getFoodWaitTimes().add(foodWaitTime);
            logger.info("Order {} is ready.Food wait time: {} minutes", order.getId(), decimalPrecision(foodWaitTime));
        };

        CourierArrivalObserver courierArrivalObserver = courier -> {
            double driverWaitTime = convertToMinutes(currentMilliSeconds() - courier.getArrivalTime());
            kitchenMediator.getCourierWaitTimes().add(driverWaitTime);
            logger.info("Courier {} has arrived.Courier wait time: {} minutes.", courier.getCourierId(), decimalPrecision(driverWaitTime));
        };

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerCourierArrivalObserver(courierArrivalObserver);

        return kitchenMediator;
    }

    public static void main(String[] args) {
        KitchenMediator kitchenMediator = getKitchenMediator();

        Simulation simulation = new Simulation(kitchenMediator);
        // Run for FIFO Strategy
        runSimulation(simulation, kitchenMediator);

        // Run for  Matched Strategy
        kitchenMediator.setDispatchCommand(new MatchedOrderDispatcherStrategy());
        runSimulation(simulation,kitchenMediator);
    }

    private static void runSimulation(Simulation simulation, KitchenMediator kitchenMediator) {
        simulation.processOrders();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        simulation.shutdown();

        kitchenMediator.printAverages();
    }

}

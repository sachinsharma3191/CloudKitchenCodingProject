package com.cloud.kitchen;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.simulation.Simulation;
import com.cloud.kitchen.strategy.MatchedOrderDispatcherStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.cloud.kitchen.util.Utility.convertToMinutes;
import static com.cloud.kitchen.util.Utility.currentMilliSeconds;
import static com.cloud.kitchen.util.Utility.decimalPrecision;

/**
 * The Main class orchestrates the simulation of order processing and courier dispatch strategies in a kitchen delivery system.
 * It initializes the simulation with two different dispatch strategies (FIFO and Matched), processes orders, and prints average statistics.
 */
public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    /**
     * Retrieves an instance of KitchenMediator configured with observers for order readiness and courier arrival.
     *
     * @return A KitchenMediator instance with registered observers.
     */
    private static KitchenMediator getKitchenMediator() {
        KitchenMediator kitchenMediator = new KitchenMediator();

        // Register observers for order readiness and driver arrival
        OrderReadyObserver orderReadyObserver = order -> {
            double foodWaitTime = convertToMinutes(currentMilliSeconds() - order.getReadyTime());
            kitchenMediator.getFoodWaitTimes().add(foodWaitTime);
            logger.info("Order {} is ready. Food wait time: {} minutes", order.getId(), decimalPrecision(foodWaitTime));
        };

        CourierArrivalObserver courierArrivalObserver = courier -> {
            double driverWaitTime = convertToMinutes(currentMilliSeconds() - courier.getArrivalTime());
            kitchenMediator.getCourierWaitTimes().add(driverWaitTime);
            logger.info("Courier {} has arrived. Courier wait time: {} minutes", courier.getCourierId(), decimalPrecision(driverWaitTime));
        };

        kitchenMediator.registerOrderReadyObserver(orderReadyObserver);
        kitchenMediator.registerCourierArrivalObserver(courierArrivalObserver);

        return kitchenMediator;
    }

    /**
     * Entry point of the application. Runs simulations for FIFO and Matched dispatch strategies,
     * processes orders, and prints average statistics for food and courier wait times.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Run simulation with FIFO strategy
        KitchenMediator kitchenMediator1 = getKitchenMediator();
        Simulation simulation1 = new Simulation(kitchenMediator1);
        runSimulation(simulation1, kitchenMediator1);

        // Run simulation with Matched strategy
        KitchenMediator kitchenMediator2 = getKitchenMediator();
        Simulation simulation2 = new Simulation(kitchenMediator2);
        kitchenMediator2.setDispatchCommand(new MatchedOrderDispatcherStrategy());
        runSimulation(simulation2, kitchenMediator2);
    }

    /**
     * Starts the simulation, processes orders, waits for a fixed duration, shuts down the simulation,
     * and prints average food and courier wait times.
     *
     * @param simulation     The Simulation instance to run.
     * @param kitchenMediator The KitchenMediator instance managing the simulation.
     */
    private static void runSimulation(Simulation simulation, KitchenMediator kitchenMediator) {
        simulation.processOrders();
        try {
            Thread.sleep(60000); // Simulate a 60-second simulation duration
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        simulation.shutdown();

        kitchenMediator.printAverages();
    }
}

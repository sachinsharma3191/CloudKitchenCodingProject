package com.cloud.kitchen.mediator;

import com.cloud.kitchen.models.Driver;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.observer.DriverArrivalObserver;
import com.cloud.kitchen.observer.MediatorSubject;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.FifoOrderDispatcherStrategy;
import com.cloud.kitchen.stragety.OrderDispatcherStrategy;
import com.cloud.kitchen.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.cloud.kitchen.util.Utility.average;
import static com.cloud.kitchen.util.Utility.decimalPrecision;

public class KitchenMediator implements MediatorSubject {

    private final static Logger logger = LogManager.getLogger(KitchenMediator.class);

    private final Queue<Order> orders = new ConcurrentLinkedQueue<>();
    private final Queue<Order> readyOrders = new ConcurrentLinkedQueue<>();
    private final Queue<Driver> waitingDrivers = new ConcurrentLinkedQueue<>();
    private final List<Double> foodWaitTimes = new CopyOnWriteArrayList<>();
    private final List<Double> driverWaitTimes = new CopyOnWriteArrayList<>();
    private final List<OrderReadyObserver> orderReadyObservers = new CopyOnWriteArrayList<>();
    private final List<DriverArrivalObserver> driverArrivalObservers = new CopyOnWriteArrayList<>();

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();


    private OrderDispatcherStrategy dispatchCommand;

    public KitchenMediator() {
        // Default to FIFO strategy
        this.dispatchCommand = new FifoOrderDispatcherStrategy();
    }

    public void setDispatchCommand(OrderDispatcherStrategy dispatchCommand) {
        this.dispatchCommand = dispatchCommand;
    }

    public Queue<Order> getOrders() {
        return orders;
    }

    public Queue<Order> getReadyOrders() {
        return readyOrders;
    }

    public Queue<Driver> getWaitingDrivers() {
        return waitingDrivers;
    }

    public List<Double> getFoodWaitTimes() {
        return foodWaitTimes;
    }

    public List<Double> getDriverWaitTimes() {
        return driverWaitTimes;
    }

    /**
     * Add Order to Queue for Processing
     *
     * @param order Object containing detail of Order
     */
    public void addOrder(Order order) {
        orders.add(order);
        prepareOrder(order);
    }

    /**
     * Prepare Order
     *
     * @param order Object containing detail of Order
     */
    private void prepareOrder(Order order) {
        scheduler.schedule(() -> {
            order.setReadyTime(System.currentTimeMillis());
            readyOrders.add(order);
            notifyOrderReadyObservers(order);
            dispatchOrder();
        }, order.getPrepTime(), TimeUnit.SECONDS);
    }

    /**
     * Add Delivery Driver to Queue of Waiting Delivery Drives
     *
     * @param driver Object containing detail of Driver
     */
    public void addDriver(Driver driver) {
        waitingDrivers.add(driver);
        notifyDriverArrivalObservers(driver);
        dispatchOrder();
    }

    @Override
    public void registerOrderReadyObserver(OrderReadyObserver observer) {
        orderReadyObservers.add(observer);
    }

    @Override
    public void registerDriverArrivalObserver(DriverArrivalObserver observer) {
        driverArrivalObservers.add(observer);
    }

    @Override
    public void notifyOrderReadyObservers(Order order) {
        orderReadyObservers.forEach(observer -> observer.onOrderReady(order));
    }

    @Override
    public void notifyDriverArrivalObservers(Driver driver) {
        driverArrivalObservers.forEach(observer -> observer.onDriverArrival(driver));
    }

    /**
     * Dispatch Orders
     */
    public void dispatchOrder() {
        while (!readyOrders.isEmpty() && !waitingDrivers.isEmpty()) {
            Order order = readyOrders.poll();
            Driver driver = waitingDrivers.poll();
            dispatchOrder(order, driver);
        }
    }

    /**
     * Dispatch the Orders and Log the time
     *
     * @param order Object containing detail of Order
     * @param driver Object containing detail of Driver
     */
    public void dispatchOrder(Order order, Driver driver) {
        double foodWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - order.getReadyTime());
        double driverWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - driver.getArrivalTime());

        foodWaitTimes.add(foodWaitTime);
        driverWaitTimes.add(driverWaitTime);

        logger.info("Driver {} is picking up order {}. Food wait time: {} minutes", driver.getDriverId(), order.getId(), decimalPrecision(foodWaitTime));
        logger.info("Driver {} waited for {} minutes.", driver.getDriverId(), decimalPrecision(driverWaitTime));

        scheduler.schedule(() -> notifyOrderReadyObservers(order), 1, TimeUnit.SECONDS);
        // Remove order from ready list and driver from waiting list
        readyOrders.remove(order);
        waitingDrivers.remove(driver);
    }

    /**
     * Print the Logs for Delivery
     */
    public void printAverages() {
        logger.info("Average statistics:");
        logger.info("Average food wait time: {} minutes", average(foodWaitTimes));
        logger.info("Average Driver wait time: {} minutes ", average(driverWaitTimes));
    }

    public void shutdown(){
        scheduler.shutdown();
    }
}

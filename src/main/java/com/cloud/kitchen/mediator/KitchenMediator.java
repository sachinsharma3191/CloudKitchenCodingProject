package com.cloud.kitchen.mediator;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Driver;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.observer.MediatorSubject;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.CourierDispatcherStrategy;
import com.cloud.kitchen.stragety.FifoCourierDispatcherStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import com.cloud.kitchen.util.Utility;

public class KitchenMediator implements MediatorSubject {

    private final static Logger logger = LogManager.getLogger(KitchenMediator.class);
    private final Queue<Order> orders = new ConcurrentLinkedQueue<>();
    private final Queue<Order> readyOrders = new ConcurrentLinkedQueue<>();
    private final Queue<Driver> waitingDrivers = new ConcurrentLinkedQueue<>();
    private final List<Double> foodWaitTimes = new CopyOnWriteArrayList<>();
    private final List<Double> courierWaitTimes = new CopyOnWriteArrayList<>();
    private final List<OrderReadyObserver> orderReadyObservers = new ArrayList<>();
    private final List<CourierArrivalObserver> courierArrivalObservers = new ArrayList<>();

    private CourierDispatcherStrategy dispatchCommand;

    public KitchenMediator() {
        // Default to FIFO strategy
        this.dispatchCommand = new FifoCourierDispatcherStrategy();
    }

    public void setDispatchCommand(CourierDispatcherStrategy dispatchCommand) {
        this.dispatchCommand = dispatchCommand;
    }

    public Queue<Order> getReadyOrders() {
        return readyOrders;
    }

    public Queue<Driver> getWaitingCouriers() {
        return waitingDrivers;
    }

    public List<Double> getFoodWaitTimes() {
        return foodWaitTimes;
    }

    public List<Double> getCourierWaitTimes() {
        return courierWaitTimes;
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
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                order.setReadyTime(System.currentTimeMillis());
                readyOrders.add(order);
                notifyOrderReadyObservers(order);
                dispatchOrders();
            }
        }, order.getPrepTime());
    }

    /**
     * Add Delivery Driver to Queue of Waiting Delivery Drives
     * 
     * @param driver  Object containing detail of Driver
     */
    public void addDriver(Driver driver) {
        waitingDrivers.add(driver);
        notifyCourierArrivalObservers(driver);
        dispatchOrders();
    }

    @Override
    public void registerOrderReadyObserver(OrderReadyObserver observer) {
        orderReadyObservers.add(observer);
    }

    @Override
    public void registerCourierArrivalObserver(CourierArrivalObserver observer) {
        courierArrivalObservers.add(observer);
    }

    @Override
    public void notifyOrderReadyObservers(Order order) {
        orderReadyObservers.forEach(observer -> observer.onOrderReady(order));
    }

    @Override
    public void notifyCourierArrivalObservers(Driver driver) {
        courierArrivalObservers.forEach(observer -> observer.onCourierArrival(driver));
    }

    /**
     *  Dispatch Orders
     */
    private void dispatchOrders() {
        while (!readyOrders.isEmpty() && !waitingDrivers.isEmpty()) {
            Order order = readyOrders.poll();
            Driver driver = waitingDrivers.poll();
            dispatchCourier(order, driver);
        }
    }

    /**
     * Dispatch the Orders and Log the time  
     *  
     * @param order     Object containing detail of Order
     * @param driver  Object containing detail of Driver
     */
    public void dispatchCourier(Order order, Driver driver) {
        double foodWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - order.getReadyTime());
        foodWaitTimes.add(foodWaitTime);
        logger.info("Driver {} is picking up order {}. Food wait time: {} minutes", driver.getDriverId(), order.getId(), foodWaitTime);

        double courierWaitTime = Utility.convertToMinutes(System.currentTimeMillis() - driver.getArrivalTime());
        courierWaitTimes.add(courierWaitTime);
        logger.info("Driver {} waited for {} minutes.", driver.getDriverId(), courierWaitTime);

        // Simulate instant delivery
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Notify observers of order completion
                notifyOrderReadyObservers(order);
            }
        }, 1);

        // Remove order from ready list and driver from waiting list
        readyOrders.remove(order);
        waitingDrivers.remove(driver);
    }


    /**
     *  Print the Logs for Delivery
     */
    public void printAverages() {
        logger.info("\nAverage statistics:");
        logger.info("Average food wait time: {} minutes", Utility.average(foodWaitTimes));
        logger.info("Average courier wait time: {} minutes ", Utility.average(courierWaitTimes));
    }
}

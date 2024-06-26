package com.cloud.kitchen.mediator;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.observer.DriverArrivalObserver;
import com.cloud.kitchen.observer.MediatorSubject;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.OrderDispatcherStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.cloud.kitchen.util.Utility.average;
import static com.cloud.kitchen.util.Utility.convertToMinutes;
import static com.cloud.kitchen.util.Utility.currentLocalDateTime;
import static com.cloud.kitchen.util.Utility.decimalPrecision;

public class KitchenMediator implements MediatorSubject {

    private final static Logger logger = LogManager.getLogger(KitchenMediator.class);

    private final Queue<Order> orders;
    private final Queue<Order> readyOrders;
    private final Queue<Courier> waitingCouriers;
    private final List<Double> foodWaitTimes;
    private final List<Double> driverWaitTimes;
    private final List<OrderReadyObserver> orderReadyObservers;
    private final List<DriverArrivalObserver> driverArrivalObservers;
    private OrderDispatcherStrategy dispatchCommand;

    public KitchenMediator() {

        this.orders = new ConcurrentLinkedQueue<>();
        this.readyOrders = new ConcurrentLinkedQueue<>();
        this.waitingCouriers = new ConcurrentLinkedQueue<>();
        this.foodWaitTimes = new CopyOnWriteArrayList<>();
        this.driverWaitTimes = new CopyOnWriteArrayList<>();
        this.orderReadyObservers = new CopyOnWriteArrayList<>();
        this.driverArrivalObservers = new CopyOnWriteArrayList<>();
        // Default to FIFO strategy
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

    public Queue<Courier> getWaitingDrivers() {
        return waitingCouriers;
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
        logger.info("Order Received {}",order);
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
                order.setReadyTime(currentLocalDateTime());
                readyOrders.add(order);
                logger.info("Order prepared: {}", order);
                notifyOrderReadyObservers(order);
                dispatchOrder();
            }
        }, order.getPrepTime() * 1000L);
    }

    /**
     * Add Delivery Courier to Queue of Waiting Delivery Drives
     *
     * @param courier Object containing detail of Courier
     */
    public void addDriver(Courier courier) {
        waitingCouriers.add(courier);
        logger.info("Courier dispatched: {}", courier);
        notifyDriverArrivalObservers(courier);
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
    public void notifyDriverArrivalObservers(Courier courier) {
        driverArrivalObservers.forEach(observer -> observer.onDriverArrival(courier));
    }

    /**
     * Dispatch Orders
     */
    public void dispatchOrder() {
        while (!readyOrders.isEmpty() && !waitingCouriers.isEmpty()) {
            Order order = readyOrders.poll();
            Courier courier = waitingCouriers.poll();
            if(order != null && courier != null) {
                dispatchOrder(order, courier);
            }
        }
    }

    /**
     * Dispatch the Orders and Log the time
     *
     * @param order Object containing detail of Order
     * @param courier Object containing detail of Courier
     */
    public void dispatchOrder(Order order, Courier courier) {
        double foodWaitTime = convertToMinutes(order.getReadyTime(), currentLocalDateTime());
        double driverWaitTime = convertToMinutes(courier.getArrivalTime(),currentLocalDateTime());

        foodWaitTimes.add(foodWaitTime);
        driverWaitTimes.add(driverWaitTime);

        logger.info("Courier {} is picking up order {}. Food wait time: {} minutes", courier.getDriverId(), order.getId(), decimalPrecision(foodWaitTime));
        logger.info("Courier {} waited for {} minutes.", courier.getDriverId(), decimalPrecision(driverWaitTime));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Notify observers of order completion
                notifyOrderReadyObservers(order);
                logger.info("Order picked up: {}", order);
            }
        }, 1000);
        // Remove order from ready list and courier from waiting list
        readyOrders.remove(order);
        waitingCouriers.remove(courier);
    }

    /**
     * Print the Logs for Delivery
     */
    public void printAverages() {
        logger.info("Average statistics:");
        logger.info("Average food wait time: {} minutes", average(foodWaitTimes));
        logger.info("Average Courier wait time: {} minutes ", average(driverWaitTimes));
    }
}

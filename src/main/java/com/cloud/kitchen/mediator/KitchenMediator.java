package com.cloud.kitchen.mediator;

import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.observer.CourierArrivalObserver;
import com.cloud.kitchen.observer.MediatorSubject;
import com.cloud.kitchen.observer.OrderReadyObserver;
import com.cloud.kitchen.stragety.FifoOrderDispatcherStrategy;
import com.cloud.kitchen.stragety.OrderDispatcherStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.cloud.kitchen.util.Utility.average;
import static com.cloud.kitchen.util.Utility.convertToMinutes;
import static com.cloud.kitchen.util.Utility.currentMilliSeconds;
import static com.cloud.kitchen.util.Utility.decimalPrecision;

public class KitchenMediator implements MediatorSubject {

    private final static Logger logger = LogManager.getLogger(KitchenMediator.class);

    private final Queue<Order> orders;
    private final Queue<Order> readyOrders;
    private final Queue<Courier> waitingCouriers;
    private final List<Double> foodWaitTimes;
    private final List<Double> courierWaitTimes;
    private final List<OrderReadyObserver> orderReadyObservers;
    private final List<CourierArrivalObserver> courierArrivalObservers;

    private final ExecutorService executorService;
    private OrderDispatcherStrategy dispatchCommand;

    public KitchenMediator() {
        this.orders = new ConcurrentLinkedQueue<>();
        this.readyOrders = new ConcurrentLinkedQueue<>();
        this.waitingCouriers = new ConcurrentLinkedQueue<>();
        this.foodWaitTimes = new CopyOnWriteArrayList<>();
        this.courierWaitTimes = new CopyOnWriteArrayList<>();
        this.orderReadyObservers = new CopyOnWriteArrayList<>();
        this.courierArrivalObservers = new CopyOnWriteArrayList<>();
        this.executorService = Executors.newCachedThreadPool();
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

    public Queue<Courier> getWaitingCouriers() {
        return waitingCouriers;
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
        logger.info("Order Received {}", order);
        prepareOrder(order);
    }

    /**
     * Prepare Order
     *
     * @param order Object containing detail of Order
     */
    private void prepareOrder(Order order) {
        executorService.submit(() -> {
            order.setReadyTime(currentMilliSeconds());
            readyOrders.add(order);
            logger.info("Order prepared: {}", order);
            notifyOrderReadyObservers(order);
            dispatchOrder();
        });
    }

    /**
     * Add Delivery Courier to Queue of Waiting Delivery Drives
     *
     * @param courier Object containing detail of Courier
     */
    public void addCourier(Courier courier) {
        waitingCouriers.add(courier);
        logger.info("Courier dispatched: {}", courier);
        notifyCourierArrivalObservers(courier);
        dispatchOrder();
    }

    @Override
    public void registerOrderReadyObserver(OrderReadyObserver observer) {
        orderReadyObservers.add(observer);
    }

    @Override
    public void registerCourierArrivalObserver(com.cloud.kitchen.observer.CourierArrivalObserver observer) {
        courierArrivalObservers.add(observer);
    }

    @Override
    public void notifyOrderReadyObservers(Order order) {
        orderReadyObservers.forEach(observer -> observer.onOrderReady(order));
    }

    @Override
    public void notifyCourierArrivalObservers(Courier courier) {
        courierArrivalObservers.forEach(observer -> observer.updateCourierArrival(courier));
    }

    /**
     * Dispatch Orders
     */
    public void dispatchOrder() {
        dispatchCommand.dispatchOrder(this, readyOrders, waitingCouriers);
    }

    /**
     * Dispatch the Orders and Log the time
     *
     * @param order Object containing detail of Order
     * @param courier Object containing detail of Courier
     */
    public void dispatchOrder(Order order, Courier courier) {
        double foodWaitTime = convertToMinutes(currentMilliSeconds() - order.getReadyTime());
        double driverWaitTime = convertToMinutes(currentMilliSeconds() - courier.getArrivalTime());

        foodWaitTimes.add(foodWaitTime);
        courierWaitTimes.add(driverWaitTime);

        logger.info("Courier {} is picking up order {}. Food wait time: {} minutes", courier.getCourierId(), order.getId(), decimalPrecision(foodWaitTime));
        logger.info("Courier {} waited for {} minutes.", courier.getCourierId(), decimalPrecision(driverWaitTime));


        // Remove order from ready list and courier from waiting list
        readyOrders.remove(order);
        waitingCouriers.remove(courier);


        executorService.submit(() -> {
            notifyOrderReadyObservers(order);
            logger.info("Order picked up: {}", order);
        });
    }

    /**
     * Print the Logs for Delivery
     */
    public void printAverages() {
        logger.info("Average statistics:");
        logger.info("Average food wait time: {} minutes", average(foodWaitTimes));
        logger.info("Average Courier wait time: {} minutes ", average(courierWaitTimes));
    }
}

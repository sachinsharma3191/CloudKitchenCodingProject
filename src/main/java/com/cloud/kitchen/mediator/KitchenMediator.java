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

/**
 * The KitchenMediator class manages the interaction between orders, couriers,
 * and dispatch strategies in a kitchen delivery system.
 * It facilitates order preparation, courier dispatch, and order pickup,
 * while also recording and reporting wait times.
 */
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

    /**
     * Constructs a KitchenMediator instance with queues for orders, ready orders,
     * waiting couriers, and lists for food wait times, courier wait times,
     * and observer registrations.
     */
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

    /**
     * Sets the dispatch strategy for order and courier dispatching.
     *
     * @param dispatchCommand The strategy to be used for dispatching orders.
     */
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
     * Adds an order to the kitchen system for processing.
     *
     * @param order The order object containing order details.
     */
    public void addOrder(Order order) {
        orders.add(order);
        logger.info("Order Received {}", order);
        prepareOrder(order);
    }

    /**
     * Prepares an order for dispatching after its preparation time has elapsed.
     *
     * @param order The order object that has been prepared.
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
     * Adds a courier to the waiting list for order pickup.
     *
     * @param courier The courier object containing courier details.
     */
    public void addCourier(Courier courier) {
        waitingCouriers.add(courier);
        logger.info("Courier dispatched: {}", courier);
        notifyCourierArrivalObservers(courier);
        dispatchOrder();
    }


    /**
     * Registers an observer for order readiness notifications.
     *
     * @param observer The observer object to be registered.
     */
    @Override
    public void registerOrderReadyObserver(OrderReadyObserver observer) {
        orderReadyObservers.add(observer);
    }

    /**
     * Registers an observer for courier arrival notifications.
     *
     * @param observer The observer object to be registered.
     */
    @Override
    public void registerCourierArrivalObserver(com.cloud.kitchen.observer.CourierArrivalObserver observer) {
        courierArrivalObservers.add(observer);
    }

    /**
     * Notifies all registered observers about the readiness of an order.
     *
     * @param order The order object that is ready for pickup.
     */
    @Override
    public void notifyOrderReadyObservers(Order order) {
        orderReadyObservers.forEach(observer -> observer.onOrderReady(order));
    }

    /**
     * Notifies all registered observers about the arrival of a courier.
     *
     * @param courier The courier object that has arrived for order pickup.
     */
    @Override
    public void notifyCourierArrivalObservers(Courier courier) {
        courierArrivalObservers.forEach(observer -> observer.updateCourierArrival(courier));
    }

    /**
     * Dispatches orders according to the configured dispatch strategy.
     */
    public void dispatchOrder() {
        dispatchCommand.dispatchOrder(this, readyOrders, waitingCouriers);
    }

    /**
     * Dispatches a specific order to a courier for pickup.
     *
     * @param order   The order object to be picked up.
     * @param courier The courier object assigned to pick up the order.
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


        // Notify observers of order completion
        notifyOrderReadyObservers(order);
        logger.info("Order picked up: {}", order);
    }

    /**
     * Prints average wait times for food preparation and courier pickup.
     */
    public void printAverages() {
        logger.info("Average statistics:");
        logger.info("Average food wait time: {} minutes", average(foodWaitTimes));
        logger.info("Average Courier wait time: {} minutes ", average(courierWaitTimes));
    }
}

package com.cloud.kitchen.mediator;

import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
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

public class KitchenMediator implements MediatorSubject {

    private final static Logger logger = LogManager.getLogger(KitchenMediator.class);

    private final Queue<Order> orders = new ConcurrentLinkedQueue<>();
    private final Queue<Order> readyOrders = new ConcurrentLinkedQueue<>();
    private final Queue<Courier> waitingCouriers = new ConcurrentLinkedQueue<>();
    private final List<Long> foodWaitTimes = new CopyOnWriteArrayList<>();
    private final List<Long> courierWaitTimes = new CopyOnWriteArrayList<>();
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

    public Queue<Courier> getWaitingCouriers() {
        return waitingCouriers;
    }

    public List<Long> getFoodWaitTimes() {
        return foodWaitTimes;
    }

    public List<Long> getCourierWaitTimes() {
        return courierWaitTimes;
    }

    public void addOrder(Order order) {
        orders.add(order);
        prepareOrder(order);
    }

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

    public void addCourier(Courier courier) {
        waitingCouriers.add(courier);
        notifyCourierArrivalObservers(courier);
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
        for (OrderReadyObserver observer : orderReadyObservers) {
            observer.onOrderReady(order);
        }
    }

    @Override
    public void notifyCourierArrivalObservers(Courier courier) {
        for (CourierArrivalObserver observer : courierArrivalObservers) {
            observer.onCourierArrival(courier);
        }
    }

    private void dispatchOrders() {
        while (!readyOrders.isEmpty() && !waitingCouriers.isEmpty()) {
            Order order = readyOrders.poll();
            Courier courier = waitingCouriers.poll();

            dispatchCourier(order, courier);
        }
    }

    public void dispatchCourier(Order order, Courier courier) {
        long foodWaitTime = System.currentTimeMillis() - order.getReadyTime();
        foodWaitTimes.add(foodWaitTime);
        logger.info("Courier {} is picking up order {}. Food wait time: {} ms", courier.getCourierId(), order.getId(), foodWaitTime);

        long courierWaitTime = System.currentTimeMillis() - courier.getArrivalTime();
        courierWaitTimes.add(courierWaitTime);
        logger.info("Courier {} waited for {} ms.", courier.getCourierId(), courierWaitTime);

        // Simulate instant delivery
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Notify observers of order completion
                notifyOrderReadyObservers(order);
            }
        }, 1);

        // Remove order from ready list and courier from waiting list
        readyOrders.remove(order);
        waitingCouriers.remove(courier);
    }

    public void printAverages() {
        double avgFoodWaitTime = foodWaitTimes.stream().mapToLong(Long::longValue).average().orElse(0);
        double avgCourierWaitTime = courierWaitTimes.stream().mapToLong(Long::longValue).average().orElse(0);

        logger.info("\nAverage statistics:");
        logger.info("Average food wait time: {} ms", avgFoodWaitTime);
        logger.info("Average courier wait time: {} ms ", avgCourierWaitTime);
    }
}

package com.cloud.kitchen.queue;

import java.util.Queue;
import com.cloud.kitchen.models.Order;
import java.util.concurrent.ConcurrentLinkedQueue;


public class OrderQueue {
    private static OrderQueue instance;
    private final Queue<Order> orders = new ConcurrentLinkedQueue<>();

    private OrderQueue() {}

    public static synchronized OrderQueue getInstance() {
        if (instance == null) {
            instance = new OrderQueue();
        }
        return instance;
    }

    public synchronized void addOrder(Order order) {
        orders.add(order);
    }

    public synchronized Order getNextOrder() {
        return orders.poll();
    }

    public synchronized boolean hasOrders() {
        return !orders.isEmpty();
    }
}
package com.cloud.kitchen.queue;


import java.util.Queue;
import com.cloud.kitchen.models.Courier;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CourierQueue {
    private static CourierQueue instance;
    private final Queue<Courier> couriers = new ConcurrentLinkedQueue<>();

    private CourierQueue() {}

    public static synchronized CourierQueue getInstance() {
        if (instance == null) {
            instance = new CourierQueue();
        }
        return instance;
    }

    public synchronized void addCourier(Courier courier) {
        couriers.add(courier);
    }

    public synchronized Courier getNextCourier() {
        return couriers.poll();
    }

    public synchronized boolean hasCouriers() {
        return !couriers.isEmpty();
    }
}

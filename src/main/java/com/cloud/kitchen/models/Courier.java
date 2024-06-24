package com.cloud.kitchen.models;

import java.util.concurrent.atomic.AtomicInteger;

public class Courier {
    private static AtomicInteger courierIdCounter = new AtomicInteger(0);
    private final int courierId;
    private long arrivalTime;

    public Courier() {
        this.courierId = courierIdCounter.incrementAndGet();
    }

    public Courier(int courierId) {
        this.courierId = courierId;
    }

    public int getCourierId() {
        return courierId;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    // Method to check if the courier matches a specific order
    public boolean matchesOrder(Order order) {
        return true;
    }
}

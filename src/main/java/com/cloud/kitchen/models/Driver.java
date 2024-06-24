package com.cloud.kitchen.models;

public class Driver {
    private final int driverId;
    private long arrivalTime;

    private Driver(int driverId) {
        this.driverId = driverId;
        this.arrivalTime = System.currentTimeMillis();
    }

    public static Driver createDriver(int driverId) {
        return new Driver(driverId);
    }

    public int getDriverId() {
        return driverId;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    // Method to check if the driver matches a specific order
    public boolean matchesOrder(Order order) {
        return true;
    }
}

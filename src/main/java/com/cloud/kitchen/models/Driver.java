package com.cloud.kitchen.models;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Driver driver = (Driver) o;
        return driverId == driver.driverId && arrivalTime == driver.arrivalTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, arrivalTime);
    }
}

package com.cloud.kitchen.models;

import java.util.Objects;
import java.time.LocalDateTime;

import static com.cloud.kitchen.util.Utility.currentLocalDateTime;

public class Courier {
    private final int driverId;
    private LocalDateTime arrivalTime;
    private final LocalDateTime dispatchTime;

    private Courier(int driverId) {
        this.driverId = driverId;
        this.arrivalTime = currentLocalDateTime();
        this.dispatchTime = currentLocalDateTime();
    }

    public static Courier createDriver(int driverId) {
        return new Courier(driverId);
    }

    public int getDriverId() {
        return driverId;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    // Method to check if the driver matches a specific order
    public boolean matchesOrder(Order order) {
        return true;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Courier courier = (Courier) o;
        return driverId == courier.driverId && arrivalTime == courier.arrivalTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(driverId, arrivalTime);
    }

    @Override
    public String toString() {
        return "Courier{" +
                "driverId=" + driverId +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}

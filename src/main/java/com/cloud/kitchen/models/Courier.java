package com.cloud.kitchen.models;

import java.util.Objects;

import static com.cloud.kitchen.util.Utility.currentMilliSeconds;

public class   Courier {
    private final int courierId;
    private final long arrivalTime;
    private final long dispatchTime;

    private Order assignedOrder;

    public Courier(int courierId) {
        this.courierId = courierId;
        this.arrivalTime = currentMilliSeconds();
        this.dispatchTime = currentMilliSeconds();
    }

    public static Courier createCourier(int courierId) {
        return new Courier(courierId);
    }

    public int getCourierId() {
        return courierId;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public long getDispatchTime() {
        return dispatchTime;
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
        return courierId == courier.courierId && arrivalTime == courier.arrivalTime && dispatchTime == courier.dispatchTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courierId, arrivalTime, dispatchTime);
    }


    public void pickUpOrder(Order assignedOrder) {
            System.out.println("Courier  " + courierId + " picked up order " + assignedOrder.getId());
    }

    @Override
    public String toString() {
        return "Courier{" +
                "courierId=" + courierId +
                ", arrivalTime=" + arrivalTime +
                ", dispatchTime=" + dispatchTime +
                '}';
    }
}

package com.cloud.kitchen.models;

import java.util.Objects;

import static com.cloud.kitchen.util.Utility.currentMilliSeconds;

/**
 * The Courier class represents a delivery courier in the kitchen system.
 * Each courier has an identifier and arrival time.
 */
public class Courier {
    private final int courierId;
    private final long arrivalTime;

    /**
     * Constructs a Courier object with a specified courier ID.
     *
     * @param courierId The unique identifier of the courier.
     */
    public Courier(int courierId) {
        this.courierId = courierId;
        this.arrivalTime = currentMilliSeconds();
}

    public static Courier createCourier(int courierId) {
        return new Courier(courierId);
    }

    /**
     * Retrieves the unique identifier of the courier.
     *
     * @return The unique identifier of the courier.
     */
    public int getCourierId() {
        return courierId;
    }

    /**
     * Retrieves the time when the courier arrived at the kitchen.
     *
     * @return The timestamp when the courier arrived, in milliseconds.
     */
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
        Courier courier = (Courier) o;
        return courierId == courier.courierId && arrivalTime == courier.arrivalTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(courierId, arrivalTime);
    }


    @Override
    public String toString() {
        return "Courier{" +
                "courierId=" + courierId +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}

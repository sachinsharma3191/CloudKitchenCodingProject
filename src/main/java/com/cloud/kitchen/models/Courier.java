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

    /**
     * Creates a new Courier object with the specified courier ID.
     *
     * @param courierId The unique identifier of the courier.
     * @return A new Courier object with the specified ID.
     */
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

    /**
     * Checks if this Courier object matches a specific order.
     * Currently, this method returns true for all orders.
     *
     * @param order The order object to check against.
     * @return Always returns true.
     */
    public boolean matchesOrder(Order order) {
        // This method can be expanded to implement actual matching logic based on the order
        return true;
    }

    /**
     * Compares this Courier object to another object for equality.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal (same class and same values), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Courier courier = (Courier) o;
        return courierId == courier.courierId && arrivalTime == courier.arrivalTime;
    }

    /**
     * Generates a hash code for this Courier object.
     *
     * @return The hash code based on the courierId and arrivalTime fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(courierId, arrivalTime);
    }

    /**
     * Returns a string representation of the Courier object.
     *
     * @return A string representation containing courierId and arrivalTime.
     */
    @Override
    public String toString() {
        return "Courier{" +
                "courierId=" + courierId +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}

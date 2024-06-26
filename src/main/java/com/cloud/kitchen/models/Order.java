package com.cloud.kitchen.models;

import java.util.Objects;

/**
 * The Order class represents a food order in the kitchen system.
 * Each order has an identifier, name, preparation time, and ready time.
 */
public class Order {
    private String id;
    private String name;
    private int prepTime;
    private long readyTime;

    /**
     * Constructs an empty Order object.
     */
    public Order() {
    }

    /**
     * Constructs an Order object with specified attributes.
     *
     * @param id       The unique identifier of the order.
     * @param name     The name of the food item in the order.
     * @param prepTime The preparation time required for the food item.
     */
    public Order(String id, String name, int prepTime) {
        this.id = id;
        this.name = name;
        this.prepTime = prepTime;
    }

    /**
     * Retrieves the unique identifier of the order.
     *
     * @return The unique identifier of the order.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the order.
     *
     * @param id The unique identifier to set for the order.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the food item in the order.
     *
     * @return The name of the food item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the food item in the order.
     *
     * @param name The name of the food item to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the preparation time required for the food item.
     *
     * @return The preparation time in minutes.
     */
    public int getPrepTime() {
        return prepTime;
    }

    /**
     * Sets the preparation time required for the food item.
     *
     * @param prepTime The preparation time in minutes to set.
     */
    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    /**
     * Retrieves the time when the order was marked as ready for pickup.
     *
     * @return The timestamp when the order was ready, in milliseconds.
     */
    public long getReadyTime() {
        return readyTime;
    }

    /**
     * Sets the time when the order is marked as ready for pickup.
     *
     * @param readyTime The timestamp when the order is ready, in milliseconds.
     */
    public void setReadyTime(long readyTime) {
        this.readyTime = readyTime;
    }

    /**
     * Compares this Order object to another object for equality.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal (same class and same values), false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return readyTime == order.readyTime && prepTime == order.prepTime && Objects.equals(id, order.id) && Objects.equals(name, order.name);
    }

    /**
     * Generates a hash code for this Order object.
     *
     * @return The hash code based on id, name, prepTime, and readyTime fields.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, readyTime, prepTime);
    }

    /**
     * Returns a string representation of the Order object.
     *
     * @return A string representation containing id, name, prepTime, and readyTime.
     */
    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", prepTime=" + prepTime +
                ", readyTime=" + readyTime +
                '}';
    }
}

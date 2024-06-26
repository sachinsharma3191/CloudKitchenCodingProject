package com.cloud.kitchen.models;

import java.util.Objects;

/**
 * The Courier class represents a delivery courier in the kitchen system.
 * Each courier has an identifier and arrival time.
 */
public class Order {
    private String id;
    private String name;
    private int prepTime;
    private long readyTime;

    public Order() {
    }

    public Order(String id, String name, int prepTime) {
        this.id = id;
        this.name = name;
        this.prepTime = prepTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }



    public long getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(long readyTime) {
        this.readyTime = readyTime;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return  readyTime == order.readyTime && prepTime == order.prepTime && Objects.equals(id, order.id) && Objects.equals(name, order.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name,readyTime, prepTime);
    }

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

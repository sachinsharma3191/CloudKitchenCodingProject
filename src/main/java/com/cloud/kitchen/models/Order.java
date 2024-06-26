package com.cloud.kitchen.models;

import java.util.Objects;
import  java.time.LocalDateTime;

import static com.cloud.kitchen.util.Utility.currentLocalDateTime;

public class Order {
    private String id;
    private String name;
    private LocalDateTime orderReceivedTime;
    private LocalDateTime readyTime;
    private int prepTime;

    public Order() {
    }

    public Order(String id, String name, int prepTime,LocalDateTime readyTime) {
        this.id = id;
        this.name = name;
        this.prepTime = prepTime;
        this.readyTime = readyTime;
        this.orderReceivedTime = currentLocalDateTime();
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

    public LocalDateTime getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(LocalDateTime readyTime) {
        this.readyTime = readyTime;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return prepTime == order.prepTime && readyTime == order.readyTime && Objects.equals(id, order.id) && Objects.equals(name, order.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, prepTime, readyTime);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", orderReceivedTime=" + orderReceivedTime +
                ", prepTime=" + prepTime +
                ", readyTime=" + readyTime +
                '}';
    }
}

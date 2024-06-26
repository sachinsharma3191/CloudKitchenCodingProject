package com.cloud.kitchen.models;

public class Courier {

    private final int id;
    private final long dispatchTime;

    public Courier(int id) {
        this.id = id;
        this.dispatchTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public long getDispatchTime() {
        return dispatchTime;
    }
}

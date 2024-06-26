package com.cloud.kitchen.factory;

import com.cloud.kitchen.models.Courier;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The CourierFactory class is responsible for creating instances of Courier objects with unique IDs.
 * It utilizes an AtomicInteger to generate sequential IDs for each new Courier created.
 */
public final class CourierFactory {

    private static final AtomicInteger courierId = new AtomicInteger(1);

    /**
     * Creates a new Courier instance with a unique ID generated by an AtomicInteger.
     *
     * @return A new Courier object with a unique ID.
     */
    public static Courier createCourier() {
        return Courier.createCourier(courierId.getAndIncrement());
    }
}

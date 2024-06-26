package com.cloud.kitchen.factory;

import com.cloud.kitchen.models.Courier;

import java.util.concurrent.atomic.AtomicInteger;

public final class CourierFactory {
    private static final AtomicInteger courierId = new AtomicInteger(1);

    public static Courier createCourier() {
        return Courier.createCourier(courierId.getAndIncrement());
    }
}


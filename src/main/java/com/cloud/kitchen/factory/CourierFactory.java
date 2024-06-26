package com.cloud.kitchen.factory;

import java.util.concurrent.atomic.AtomicInteger;
import com.cloud.kitchen.models.Courier;

public final class CourierFactory {
    private static final AtomicInteger driverId = new AtomicInteger(1);

    public static Courier createCourier() {
        return Courier.createDriver(driverId.getAndIncrement());
    }
}


package com.cloud.kitchen.factory;

import java.util.concurrent.atomic.AtomicInteger;
import com.cloud.kitchen.models.Driver;

public final class DriverFactory {
    private static final AtomicInteger driverId = new AtomicInteger(1);

    public static Driver createCourier() {
        return Driver.createDriver(driverId.getAndIncrement());
    }
}


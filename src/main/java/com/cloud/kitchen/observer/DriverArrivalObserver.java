package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Driver;

public interface DriverArrivalObserver {
    void onDriverArrival(Driver driver);
}

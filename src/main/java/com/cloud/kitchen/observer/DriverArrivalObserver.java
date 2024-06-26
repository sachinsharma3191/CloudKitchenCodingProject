package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Courier;

public interface DriverArrivalObserver {
    void onDriverArrival(Courier courier);
}

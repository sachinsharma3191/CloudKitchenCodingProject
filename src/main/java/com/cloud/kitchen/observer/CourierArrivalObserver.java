package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Driver;

public interface CourierArrivalObserver {
    void onCourierArrival(Driver driver);
}

package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Courier;

public interface CourierArrivalObserver {
    void updateCourierArrival(Courier courier);
}

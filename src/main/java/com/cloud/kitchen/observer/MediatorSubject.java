package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Driver;


public interface MediatorSubject {
    void registerOrderReadyObserver(OrderReadyObserver observer);
    void registerCourierArrivalObserver(CourierArrivalObserver observer);
    void notifyOrderReadyObservers(Order order);
    void notifyCourierArrivalObservers(Driver driver);
}

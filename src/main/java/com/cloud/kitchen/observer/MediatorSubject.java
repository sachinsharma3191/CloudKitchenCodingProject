package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Driver;
import com.cloud.kitchen.models.Order;


public interface MediatorSubject {
    void registerOrderReadyObserver(OrderReadyObserver observer);

    void registerDriverArrivalObserver(DriverArrivalObserver observer);

    void notifyOrderReadyObservers(Order order);

    void notifyDriverArrivalObservers(Driver driver);
}

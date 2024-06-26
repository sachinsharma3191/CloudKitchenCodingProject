package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Courier;


public interface MediatorSubject {
    void registerOrderReadyObserver(OrderReadyObserver observer);

    void registerDriverArrivalObserver(DriverArrivalObserver observer);

    void notifyOrderReadyObservers(Order order);

    void notifyDriverArrivalObservers(Courier courier);
}

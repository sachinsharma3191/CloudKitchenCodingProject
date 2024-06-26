package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Courier;

/**
 * The MediatorSubject interface defines methods for registering observers
 * and notifying them about order and courier events in a kitchen delivery system.
 */
public interface MediatorSubject {

    /**
     * Registers an observer to receive notifications when orders are ready for pickup.
     *
     * @param observer The observer object implementing OrderReadyObserver.
     */
    void registerOrderReadyObserver(OrderReadyObserver observer);

    /**
     * Registers an observer to receive notifications when couriers arrive for order pickup.
     *
     * @param observer The observer object implementing CourierArrivalObserver.
     */
    void registerCourierArrivalObserver(CourierArrivalObserver observer);

    /**
     * Notifies all registered observers about the readiness of an order for pickup.
     *
     * @param order The Order object that is ready for pickup.
     */
    void notifyOrderReadyObservers(Order order);

    /**
     * Notifies all registered observers about the arrival of a courier for order pickup.
     *
     * @param courier The Courier object that has arrived for order pickup.
     */
    void notifyCourierArrivalObservers(Courier courier);
}

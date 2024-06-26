package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Courier;

/**
 * The CourierArrivalObserver interface represents an observer that
 * receives notifications when a courier arrives at the kitchen.
 */
public interface CourierArrivalObserver {

    /**
     * Called when a courier arrives in the kitchen.
     *
     * @param courier The courier that has arrived.
     */
    void updateCourierArrival(Courier courier);
}

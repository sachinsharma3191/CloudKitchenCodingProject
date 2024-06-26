package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Order;

/**
 * The OrderReadyObserver interface represents an observer that
 * receives notifications when an order is ready for pickup.
 */
public interface OrderReadyObserver {

    /**
     * Called when an order is ready for pickup.
     *
     * @param order The order that is ready.
     */
    void onOrderReady(Order order);
}
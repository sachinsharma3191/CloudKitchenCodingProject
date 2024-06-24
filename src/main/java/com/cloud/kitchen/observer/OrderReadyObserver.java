package com.cloud.kitchen.observer;

import com.cloud.kitchen.models.Order;

public interface OrderReadyObserver {
    void onOrderReady(Order order);
}

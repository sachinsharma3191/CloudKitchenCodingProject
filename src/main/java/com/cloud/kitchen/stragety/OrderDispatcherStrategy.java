package com.cloud.kitchen.stragety;

import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.mediator.KitchenMediator;
import java.util.Queue;

public interface OrderDispatcherStrategy {
    void dispatchOrder(KitchenMediator mediator,Queue<Order> readyOrders, Queue<Courier> waitingCouriers);
}
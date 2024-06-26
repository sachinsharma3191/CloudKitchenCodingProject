package com.cloud.kitchen.strategy;

import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.mediator.KitchenMediator;
import java.util.Queue;

/**
 * The OrderDispatcherStrategy interface defines the contract for dispatching orders
 * to couriers based on different strategies.
 */
public interface OrderDispatcherStrategy {
    /**
     * Dispatches orders to available couriers based on the strategy implemented.
     *
     * @param mediator The kitchen mediator managing orders and couriers.
     * @param readyOrders The queue of orders ready for pickup.
     * @param waitingCouriers The queue of couriers waiting to pick up orders.
     */
    void dispatchOrder(KitchenMediator mediator,Queue<Order> readyOrders, Queue<Courier> waitingCouriers);
}
package com.cloud.kitchen.stragety;

import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.mediator.KitchenMediator;
import java.util.Queue;


public class FifoOrderDispatcherStrategy implements OrderDispatcherStrategy {

    @Override
    public void dispatchOrder(KitchenMediator mediator, Queue<Order> readyOrders,Queue<Courier> waitingCouriers) {
        while (!waitingCouriers.isEmpty()) {
            Courier courier = waitingCouriers.poll();
            Order order = mediator.getReadyOrders().poll();
            if (order != null && courier.matchesOrder(order)) {
                mediator.dispatchOrder(order, courier);
            } else {
                mediator.getWaitingCouriers().add(courier);
                break; // Exit the loop if no orders are available
            }
        }

        while (!readyOrders.isEmpty() && !waitingCouriers.isEmpty()) {
            Order order = readyOrders.poll();
            Courier courier = waitingCouriers.poll();
            if(order != null && courier != null) {
                mediator.dispatchOrder(order, courier);
            }
        }
    }
}

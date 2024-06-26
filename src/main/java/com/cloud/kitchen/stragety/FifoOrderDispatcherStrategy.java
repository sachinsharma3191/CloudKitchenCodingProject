package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Courier;

public class FifoOrderDispatcherStrategy implements OrderDispatcherStrategy {

    @Override
    public void dispatchOrder(KitchenMediator mediator, Courier courier) {
        Order order = mediator.getReadyOrders().poll();
        if (order != null) {
            mediator.dispatchOrder(order, courier);
        } else {
            mediator.getWaitingDrivers().add(courier);
        }
    }

}

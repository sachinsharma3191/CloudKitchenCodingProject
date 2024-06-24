package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Driver;
import com.cloud.kitchen.models.Order;

public class FifoOrderDispatcherStrategy implements OrderDispatcherStrategy {

    @Override
    public void dispatchOrder(KitchenMediator mediator, Driver driver) {
        Order order = mediator.getReadyOrders().poll();
        if (order != null) {
            mediator.dispatchOrder(order, driver);
        } else {
            mediator.getWaitingDrivers().add(driver);
        }
    }

}

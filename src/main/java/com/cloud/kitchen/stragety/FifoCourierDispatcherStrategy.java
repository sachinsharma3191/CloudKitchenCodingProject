package com.cloud.kitchen.stragety;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Driver;
import com.cloud.kitchen.mediator.KitchenMediator;

public class FifoCourierDispatcherStrategy implements CourierDispatcherStrategy{

    @Override
    public void dispatchCourier(KitchenMediator mediator, Driver driver) {
        Order order = mediator.getReadyOrders().poll();
        if (order != null) {
            mediator.dispatchCourier(order, driver);
        } else {
            mediator.getWaitingCouriers().add(driver);
        }
    }

}

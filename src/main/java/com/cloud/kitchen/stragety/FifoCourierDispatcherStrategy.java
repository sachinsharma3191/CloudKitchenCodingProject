package com.cloud.kitchen.stragety;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.mediator.KitchenMediator;

public class FifoCourierDispatcherStrategy implements CourierDispatcherStrategy{

    @Override
    public void dispatchCourier(KitchenMediator mediator,Courier courier) {
        Order order = mediator.getReadyOrders().poll();
        if (order != null) {
            mediator.dispatchCourier(order, courier);
        } else {
            mediator.getWaitingCouriers().add(courier);
        }
    }

}

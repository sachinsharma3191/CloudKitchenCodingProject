package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;

public class MatchedCourierDispatcherStrategy implements CourierDispatcherStrategy {
    @Override
    public void dispatchCourier(KitchenMediator mediator, Courier courier) {
        Order order = mediator.getReadyOrders().poll();
        if (order != null && courier.matchesOrder(order)) {
            mediator.dispatchCourier(order, courier);
        } else {
            mediator.getWaitingCouriers().add(courier);
        }
    }
}

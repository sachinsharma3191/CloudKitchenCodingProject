package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.models.Driver;

public class MatchedCourierDispatcherStrategy implements CourierDispatcherStrategy {
    @Override
    public void dispatchCourier(KitchenMediator mediator, Driver driver) {
        Order order = mediator.getReadyOrders().poll();
        if (order != null && driver.matchesOrder(order)) {
            mediator.dispatchCourier(order, driver);
        } else {
            mediator.getWaitingCouriers().add(driver);
        }
    }
}

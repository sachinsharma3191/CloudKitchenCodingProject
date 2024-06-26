package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Courier;

public interface OrderDispatcherStrategy {
    void dispatchOrder(KitchenMediator kitchenMediator, Courier courier);
}

package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Driver;

public interface OrderDispatcherStrategy {
    void dispatchOrder(KitchenMediator kitchenMediator, Driver driver);
}

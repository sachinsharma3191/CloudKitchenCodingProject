package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Driver;

public interface CourierDispatcherStrategy {
    void dispatchCourier(KitchenMediator kitchenMediator, Driver driver);
}

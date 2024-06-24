package com.cloud.kitchen.stragety;

import com.cloud.kitchen.mediator.KitchenMediator;
import com.cloud.kitchen.models.Courier;

public interface CourierDispatcherStrategy {
    void dispatchCourier(KitchenMediator kitchenMediator, Courier courier);
}

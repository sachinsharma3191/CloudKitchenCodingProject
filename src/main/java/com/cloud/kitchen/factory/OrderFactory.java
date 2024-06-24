package com.cloud.kitchen.factory;

import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.util.JsonUtility;

import java.util.concurrent.ConcurrentLinkedQueue;

public class OrderFactory {
    public static ConcurrentLinkedQueue<Order> readOrders(){
        return new ConcurrentLinkedQueue(JsonUtility.readOrders());
    }
}


package com.cloud.kitchen.stragety;

import com.cloud.kitchen.models.Courier;
import com.cloud.kitchen.models.Order;
import com.cloud.kitchen.mediator.KitchenMediator;
import java.util.Queue;

/**
 * Implements the Matched dispatch strategy, where each courier is matched with a specific order.
 */
public class MatchedOrderDispatcherStrategy implements OrderDispatcherStrategy {
    /**
     * Dispatches orders to available couriers based on the FIFO strategy.
     *
     * @param mediator The kitchen mediator managing orders and couriers.
     * @param readyOrders The queue of orders ready for pickup.
     * @param waitingCouriers The queue of couriers waiting to pick up orders.
     */
    @Override
    public void dispatchOrder(KitchenMediator mediator, Queue<Order> readyOrders, Queue<Courier> waitingCouriers) {
        /*
        This loop runs as long as there are couriers in the waitingCouriers queue.
        It polls (removes and returns) the next courier from the waitingCouriers queue.
        It then attempts to poll the next order from the readyOrders queue using mediator.getReadyOrders().poll().
        If an order is available (order != null) and courier matches the order, it calls mediator.dispatchCourier(order, courier) to dispatch the courier to the order.
        If no order is available (order == null), it adds the courier back to the waitingCouriers queue and breaks out of the loop. This ensures that couriers that didn't get an order are put back in the waiting line.
         */
        while (!waitingCouriers.isEmpty()) {
            Courier courier = waitingCouriers.poll();
            Order order = mediator.getReadyOrders().poll();
            if (order != null && courier.matchesOrder(order)) {
                mediator.dispatchOrder(order, courier);
            } else {
                mediator.getWaitingCouriers().add(courier);
                break; // Exit the loop if no orders are available
            }
        }

        /*
        This loop runs as long as there are orders in the readyOrders queue and couriers in the waitingCouriers queue.
        It polls the next order from the readyOrders queue.
        It polls the next courier from the waitingCouriers queue.
        If both an order and a courier are available (order != null && courier != null), it calls mediator.dispatchCourier(order, courier) to dispatch the courier to the order.
         */
        while (!readyOrders.isEmpty() && !waitingCouriers.isEmpty()) {
            Order order = readyOrders.poll();
            Courier courier = waitingCouriers.poll();
            if(order != null && courier != null) {
                mediator.dispatchOrder(order, courier);
            }
        }
    }
}

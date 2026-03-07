package org.example;

import java.util.List;

/**
 * Courier - takes pizzas from storage and delivers them to customers.
 */
public class Courier implements Runnable {
    private final int id;
    private final int trunkCapacity;
    private final int deliveryTimeMs;
    private final Storage storage;
    private final List<Order> interruptedOrders;

    /**
     * Creates a new courier.
     *
     * @param id                courier identifier
     * @param trunkCapacity     trunk capacity (max pizzas per delivery)
     * @param deliveryTimeMs    delivery time (ms)
     * @param storage           finished product storage
     * @param interruptedOrders list to save interrupted orders
     */
    public Courier(int id, int trunkCapacity, int deliveryTimeMs,
                   Storage storage, List<Order> interruptedOrders) {
        this.id = id;
        this.trunkCapacity = trunkCapacity;
        this.deliveryTimeMs = deliveryTimeMs;
        this.storage = storage;
        this.interruptedOrders = interruptedOrders;
    }

    @Override
    public void run() {
        List<Order> currentOrders = null;
        while (true) {
            try {
                currentOrders = storage.takeUpTo(trunkCapacity);
                if (currentOrders.isEmpty()) {
                    // Storage is closed and empty - stop working
                    break;
                }

                // Set status to "Delivering"
                for (Order order : currentOrders) {
                    order.setState(OrderState.DELIVERING);
                }

                // Simulate delivery
                Thread.sleep(deliveryTimeMs);

                // Delivered
                for (Order order : currentOrders) {
                    order.setState(OrderState.DELIVERED);
                }
                currentOrders = null; // Orders successfully delivered
            } catch (InterruptedException e) {
                System.out.println("Courier #" + id + " interrupted.");
                if (currentOrders != null) {
                    interruptedOrders.addAll(currentOrders);
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Courier #" + id + " finished work.");
    }

    /**
     * Returns the courier's identifier.
     *
     * @return courier id
     */
    public int getCourierId() {
        return id;
    }

    /**
     * Returns the trunk capacity.
     *
     * @return trunk capacity
     */
    public int getTrunkCapacity() {
        return trunkCapacity;
    }

    /**
     * Returns the delivery time in milliseconds.
     *
     * @return delivery time in ms
     */
    public int getDeliveryTimeMs() {
        return deliveryTimeMs;
    }
}

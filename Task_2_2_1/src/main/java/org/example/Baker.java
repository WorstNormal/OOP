package org.example;

import java.util.List;

/**
 * Baker - takes orders from the queue, cooks pizza and puts it in storage.
 */
public class Baker implements Runnable {
    private final int id;
    private final int cookingTimeMs;
    private final SharedQueue<Order> orderQueue;
    private final Storage storage;
    private final List<Order> interruptedOrders;

    /**
     * Creates a new baker.
     *
     * @param id                baker identifier
     * @param cookingTimeMs     time to cook one pizza (ms)
     * @param orderQueue        shared order queue
     * @param storage           finished product storage
     * @param interruptedOrders list to save interrupted orders
     */
    public Baker(int id, int cookingTimeMs, SharedQueue<Order> orderQueue,
                 Storage storage, List<Order> interruptedOrders) {
        this.id = id;
        this.cookingTimeMs = cookingTimeMs;
        this.orderQueue = orderQueue;
        this.storage = storage;
        this.interruptedOrders = interruptedOrders;
    }

    @Override
    public void run() {
        Order currentOrder = null;
        while (true) {
            try {
                currentOrder = orderQueue.take();
                if (currentOrder == null) {
                    // Queue is closed and empty - stop working
                    break;
                }

                // Start cooking
                currentOrder.setState(OrderState.COOKING);
                Thread.sleep(cookingTimeMs);

                currentOrder.setState(OrderState.COOKED);

                // Try to put in storage (wait if storage is full)
                if (!storage.put(currentOrder)) {
                    // Storage is closed - save the order
                    interruptedOrders.add(currentOrder);
                    break;
                }
                currentOrder = null; // Order successfully processed
            } catch (InterruptedException e) {
                System.out.println("Baker #" + id + " interrupted.");
                if (currentOrder != null) {
                    interruptedOrders.add(currentOrder);
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Baker #" + id + " finished work.");
    }

    /**
     * Returns the baker's identifier.
     *
     * @return baker id
     */
    public int getBakerId() {
        return id;
    }

    /**
     * Returns the cooking time in milliseconds.
     *
     * @return cooking time in ms
     */
    public int getCookingTimeMs() {
        return cookingTimeMs;
    }
}

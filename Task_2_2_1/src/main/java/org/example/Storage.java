package org.example;

import java.util.List;

/**
 * Finished product storage with capacity of T pizzas.
 * Wrapper over SharedQueue with limited capacity.
 */
public class Storage {
    private final SharedQueue<Order> queue;
    private final int capacity;

    /**
     * Creates a storage with the given capacity.
     *
     * @param capacity maximum number of pizzas that can be stored
     */
    public Storage(int capacity) {
        this.capacity = capacity;
        this.queue = new SharedQueue<>(capacity);
    }

    /**
     * Puts a pizza in storage. Blocks if storage is full.
     *
     * @param order finished order
     * @return true if successful, false if storage is closed
     */
    public boolean put(Order order) {
        boolean result = queue.put(order);
        if (result) {
            order.setState(OrderState.IN_STORAGE);
        }
        return result;
    }

    /**
     * Takes up to maxCount pizzas from storage. Blocks if storage is empty.
     *
     * @param maxCount maximum number of pizzas (trunk capacity)
     * @return list of orders
     */
    public List<Order> takeUpTo(int maxCount) {
        return queue.takeUpTo(maxCount);
    }

    /**
     * Closes storage (wakes up all waiting threads).
     */
    public void close() {
        queue.close();
    }

    /**
     * Checks if storage is closed and empty.
     *
     * @return true if closed and empty
     */
    public boolean isClosedAndEmpty() {
        return queue.isClosedAndEmpty();
    }

    /**
     * Checks if storage is closed.
     *
     * @return true if closed
     */
    public boolean isClosed() {
        return queue.isClosed();
    }

    /**
     * Returns the current number of pizzas in storage.
     *
     * @return current size
     */
    public int size() {
        return queue.size();
    }

    /**
     * Returns the storage capacity.
     *
     * @return capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Gets all remaining orders in storage (for serialization).
     *
     * @return list of all remaining orders
     */
    public List<Order> drainAll() {
        return queue.drainAll();
    }
}

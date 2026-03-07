package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Thread-safe queue based on LinkedList and synchronized.
 * Implemented without using java.util.concurrent.BlockingQueue.
 *
 * @param <T> type of queue elements
 */
public class SharedQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    private final int capacity;
    private volatile boolean closed = false;

    /**
     * Creates a queue with the given maximum capacity.
     *
     * @param capacity maximum capacity (0 means unlimited)
     */
    public SharedQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Creates a queue with unlimited capacity.
     */
    public SharedQueue() {
        this(0);
    }

    /**
     * Adds an element to the queue. If the queue is full, blocks the thread
     * until space becomes available or the queue is closed.
     *
     * @param item element to add
     * @return true if element was added, false if queue is closed
     */
    public synchronized boolean put(T item) {
        while (capacity > 0 && queue.size() >= capacity && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        if (closed) {
            return false;
        }
        queue.addLast(item);
        notifyAll();
        return true;
    }

    /**
     * Removes and returns an element from the queue. If the queue is empty,
     * blocks the thread until an element becomes available or the queue is closed.
     *
     * @return element or null if queue is closed and empty
     */
    public synchronized T take() {
        while (queue.isEmpty() && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        if (queue.isEmpty()) {
            return null;
        }
        T item = queue.removeFirst();
        notifyAll();
        return item;
    }

    /**
     * Removes and returns up to maxCount elements from the queue. If the queue
     * is empty, blocks the thread until at least one element is available
     * or the queue is closed.
     *
     * @param maxCount maximum number of elements to remove
     * @return list of elements (may be empty if queue is closed)
     */
    public synchronized List<T> takeUpTo(int maxCount) {
        while (queue.isEmpty() && !closed) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ArrayList<>();
            }
        }
        if (queue.isEmpty()) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        int count = Math.min(maxCount, queue.size());
        for (int i = 0; i < count; i++) {
            result.add(queue.removeFirst());
        }
        notifyAll();
        return result;
    }

    /**
     * Closes the queue. All waiting threads will be woken up.
     */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    /**
     * Checks if the queue is closed.
     *
     * @return true if closed
     */
    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * Checks if the queue is closed and empty.
     *
     * @return true if closed and empty
     */
    public synchronized boolean isClosedAndEmpty() {
        return closed && queue.isEmpty();
    }

    /**
     * Returns the current size of the queue.
     *
     * @return current size
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * Returns all remaining elements (for serializing unfinished orders).
     *
     * @return list of all remaining elements
     */
    public synchronized List<T> drainAll() {
        List<T> result = new ArrayList<>(queue);
        queue.clear();
        notifyAll();
        return result;
    }
}

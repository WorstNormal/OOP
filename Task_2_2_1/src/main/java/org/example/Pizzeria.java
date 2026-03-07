package org.example;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main pizzeria class - manages the production process.
 * Shutdown strategy: stop accepting orders, all remaining orders
 * in the queue and storage are serialized to a file for completion the next day.
 */
public class Pizzeria {
    private final PizzeriaConfig config;
    private final SharedQueue<Order> orderQueue;
    private final Storage storage;
    private final List<Thread> bakerThreads = new ArrayList<>();
    private final List<Thread> courierThreads = new ArrayList<>();
    private final List<Order> interruptedOrders =
            java.util.Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger orderCounter = new AtomicInteger(0);
    private volatile boolean acceptingOrders = true;

    /**
     * Creates a new pizzeria with the given configuration.
     *
     * @param config pizzeria configuration
     */
    public Pizzeria(PizzeriaConfig config) {
        this.config = config;
        this.orderQueue = new SharedQueue<>();
        this.storage = new Storage(config.getStorageCapacity());
    }

    /**
     * Starts the pizzeria: creates baker and courier threads.
     */
    public void start() {
        System.out.println("=== Pizzeria is open! ===");
        System.out.println("Storage capacity: " + config.getStorageCapacity() + " pizzas");
        System.out.println("Bakers: " + config.getBakers().length);
        System.out.println("Couriers: " + config.getCouriers().length);
        System.out.println("Working time: " + config.getWorkingTimeMs() + " ms");
        System.out.println("=========================\n");

        // Create and start baker threads
        for (PizzeriaConfig.BakerConfig bc : config.getBakers()) {
            Baker baker = new Baker(bc.getId(), bc.getCookingTimeMs(),
                    orderQueue, storage, interruptedOrders);
            Thread t = new Thread(baker, "Baker-" + bc.getId());
            t.setDaemon(true);
            bakerThreads.add(t);
            t.start();
        }

        // Create and start courier threads
        for (PizzeriaConfig.CourierConfig cc : config.getCouriers()) {
            Courier courier = new Courier(cc.getId(), cc.getTrunkCapacity(),
                    cc.getDeliveryTimeMs(), storage, interruptedOrders);
            Thread t = new Thread(courier, "Courier-" + cc.getId());
            t.setDaemon(true);
            courierThreads.add(t);
            t.start();
        }
    }

    /**
     * Places a new order in the queue.
     *
     * @return created order or null if orders are not being accepted
     */
    public Order placeOrder() {
        if (!acceptingOrders) {
            System.out.println("Orders are no longer accepted!");
            return null;
        }
        int id = orderCounter.incrementAndGet();
        Order order = new Order(id);
        order.setState(OrderState.QUEUED);
        if (!orderQueue.put(order)) {
            System.out.println("Failed to place order #" + id + " - queue is closed.");
            return null;
        }
        return order;
    }

    /**
     * Shuts down the pizzeria.
     * 1. Stops accepting new orders.
     * 2. Closes the order queue.
     * 3. Interrupts bakers (immediate stop strategy).
     * 4. Closes storage.
     * 5. Interrupts couriers.
     * 6. Serializes unfinished orders.
     */
    public void shutdown() {
        System.out.println("\n=== Pizzeria is closing! ===");

        // 1. Stop accepting orders
        acceptingOrders = false;

        // 2. Close the queue
        orderQueue.close();

        // 3. Interrupt bakers and wait for them to finish
        for (Thread t : bakerThreads) {
            t.interrupt();
        }
        for (Thread t : bakerThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 4. Close storage
        storage.close();

        // 5. Interrupt couriers and wait for them to finish
        for (Thread t : courierThreads) {
            t.interrupt();
        }
        for (Thread t : courierThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 6. Serialize unfinished orders (if any remain in queue/storage)
        List<Order> unfinishedFromQueue = orderQueue.drainAll();
        List<Order> unfinishedFromStorage = storage.drainAll();
        List<Order> allUnfinished = new ArrayList<>();
        allUnfinished.addAll(unfinishedFromQueue);
        allUnfinished.addAll(unfinishedFromStorage);
        allUnfinished.addAll(interruptedOrders);

        if (!allUnfinished.isEmpty()) {
            serializeUnfinished(allUnfinished);
        }

        System.out.println("=== Pizzeria is closed! ===");
    }

    /**
     * Serializes unfinished orders to a JSON file.
     *
     * @param orders list of unfinished orders
     */
    private void serializeUnfinished(List<Order> orders) {
        System.out.println("Unfinished orders: " + orders.size());
        Gson gson = new Gson();
        List<UnfinishedOrder> data = new ArrayList<>();
        for (Order o : orders) {
            data.add(new UnfinishedOrder(o.getId(), o.getState().name()));
        }
        try (FileWriter writer = new FileWriter("unfinished_orders.json")) {
            gson.toJson(data, writer);
            System.out.println("Unfinished orders saved to unfinished_orders.json");
        } catch (IOException e) {
            System.err.println("Error serializing unfinished orders: " + e.getMessage());
        }
    }

    /**
     * Checks if the pizzeria is accepting orders.
     *
     * @return true if accepting orders
     */
    public boolean isAcceptingOrders() {
        return acceptingOrders;
    }

    /**
     * Loads pizzeria configuration from a JSON file.
     *
     * @param path path to the configuration file
     * @return loaded configuration
     * @throws IOException if the file cannot be read
     */
    public static PizzeriaConfig loadConfig(String path) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, PizzeriaConfig.class);
        }
    }

    /**
     * Helper class for serializing an unfinished order.
     */
    private static class UnfinishedOrder {
        int id;
        String state;

        UnfinishedOrder(int id, String state) {
            this.id = id;
            this.state = state;
        }
    }
}

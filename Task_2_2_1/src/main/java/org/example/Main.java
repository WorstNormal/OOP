package org.example;

/**
 * Main entry point for the pizzeria simulator.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        // Utility class
    }

    /**
     * Main method.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            // Load configuration
            PizzeriaConfig config = Pizzeria.loadConfig("config.json");

            // Create and start the pizzeria
            Pizzeria pizzeria = new Pizzeria(config);
            pizzeria.start();

            // Start thread that generates orders
            Thread orderGenerator = new Thread(() -> {
                while (pizzeria.isAcceptingOrders()) {
                    pizzeria.placeOrder();
                    try {
                        // Simulate random order arrival interval (200-700 ms)
                        Thread.sleep(200 + (long) (Math.random() * 500));
                    } catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            orderGenerator.start();

            // Pizzeria works for the specified time
            System.out.println("Pizzeria will work for " + config.getWorkingTimeMs() + " ms...");
            Thread.sleep(config.getWorkingTimeMs());

            // Shutdown
            pizzeria.shutdown();

            // Stop the order generator if it's still running
            orderGenerator.interrupt();
            orderGenerator.join();

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
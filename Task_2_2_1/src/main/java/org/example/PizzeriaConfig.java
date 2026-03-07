package org.example;

/**
 * Pizzeria configuration loaded from a JSON file.
 */
public class PizzeriaConfig {
    private BakerConfig[] bakers;
    private CourierConfig[] couriers;
    private int storageCapacity;
    private int workingTimeMs;

    /**
     * Default constructor for JSON deserialization.
     */
    public PizzeriaConfig() {
        // Default constructor for Gson
    }

    /**
     * Returns the array of baker configurations.
     *
     * @return baker configurations
     */
    public BakerConfig[] getBakers() {
        return bakers;
    }

    /**
     * Returns the array of courier configurations.
     *
     * @return courier configurations
     */
    public CourierConfig[] getCouriers() {
        return couriers;
    }

    /**
     * Returns the storage capacity.
     *
     * @return storage capacity in pizzas
     */
    public int getStorageCapacity() {
        return storageCapacity;
    }

    /**
     * Returns the working time in milliseconds.
     *
     * @return working time in ms
     */
    public int getWorkingTimeMs() {
        return workingTimeMs;
    }

    /**
     * Configuration for a single baker.
     */
    public static class BakerConfig {
        private int id;
        private int cookingTimeMs;

        /**
         * Default constructor for JSON deserialization.
         */
        public BakerConfig() {
            // Default constructor for Gson
        }

        /**
         * Returns the baker's identifier.
         *
         * @return baker id
         */
        public int getId() {
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

    /**
     * Configuration for a single courier.
     */
    public static class CourierConfig {
        private int id;
        private int trunkCapacity;
        private int deliveryTimeMs;

        /**
         * Default constructor for JSON deserialization.
         */
        public CourierConfig() {
            // Default constructor for Gson
        }

        /**
         * Returns the courier's identifier.
         *
         * @return courier id
         */
        public int getId() {
            return id;
        }

        /**
         * Returns the trunk capacity.
         *
         * @return trunk capacity in pizzas
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
}

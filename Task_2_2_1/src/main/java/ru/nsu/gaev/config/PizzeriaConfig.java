package ru.nsu.gaev.config;

/**
 * Конфигурация пиццерии, загружаемая из JSON файла.
 */
public class PizzeriaConfig {
    private BakerConfig[] bakers;
    private CourierConfig[] couriers;
    private int storageCapacity;
    private int workingTimeMs;

    /**
     * Возвращает массив конфигураций пекарей.
     *
     * @return конфигурации пекарей
     */
    public BakerConfig[] getBakers() {
        return bakers;
    }

    /**
     * Возвращает массив конфигураций курьеров.
     *
     * @return конфигурации курьеров
     */
    public CourierConfig[] getCouriers() {
        return couriers;
    }

    /**
     * Возвращает вместимость склада.
     *
     * @return вместимость склада в пиццах
     */
    public int getStorageCapacity() {
        return storageCapacity;
    }

    /**
     * Возвращает время работы в миллисекундах.
     *
     * @return время работы в мс
     */
    public int getWorkingTimeMs() {
        return workingTimeMs;
    }

    /**
     * Конфигурация отдельного пекаря.
     */
    public static class BakerConfig {
        private int id;
        private int cookingTimeMs;

        /**
         * Возвращает идентификатор пекаря.
         *
         * @return id пекаря
         */
        public int getId() {
            return id;
        }

        /**
         * Возвращает время приготовления в миллисекундах.
         *
         * @return время приготовления в мс
         */
        public int getCookingTimeMs() {
            return cookingTimeMs;
        }
    }

    /**
     * Конфигурация отдельного курьера.
     */
    public static class CourierConfig {
        private int id;
        private int trunkCapacity;
        private int deliveryTimeMs;

        /**
         * Возвращает идентификатор курьера.
         *
         * @return id курьера
         */
        public int getId() {
            return id;
        }

        /**
         * Возвращает вместимость багажника.
         *
         * @return вместимость багажника в пиццах
         */
        public int getTrunkCapacity() {
            return trunkCapacity;
        }

        /**
         * Возвращает время доставки в миллисекундах.
         *
         * @return время доставки в мс
         */
        public int getDeliveryTimeMs() {
            return deliveryTimeMs;
        }
    }
}

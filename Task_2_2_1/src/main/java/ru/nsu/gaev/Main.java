package ru.nsu.gaev;

import ru.nsu.gaev.config.PizzeriaConfig;

/**
 * Главная точка входа в симулятор пиццерии.
 */
public class Main {

    /**
     * Основной метод.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        try {
            // Загрузка конфигурации
            PizzeriaConfig config = Pizzeria.loadConfig("config.json");

            // Создание и запуск пиццерии
            Pizzeria pizzeria = new Pizzeria(config);
            pizzeria.start();

            // Запуск потока, генерирующего заказы
            Thread orderGenerator = new Thread(() -> {
                while (pizzeria.isAcceptingOrders()) {
                    pizzeria.placeOrder();
                    try {
                        // Имитация случайного интервала поступления заявок (200-700 мс)
                        Thread.sleep(200 + (long) (Math.random() * 500));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });
            orderGenerator.start();

            // Пиццерия работает указанный время
            System.out.println("Пиццерия будет работать " + config.getWorkingTimeMs() + " мс...");
            Thread.sleep(config.getWorkingTimeMs());

            // Остановка
            pizzeria.shutdown();

            // Остановка генератора заказов, если он все еще работает
            orderGenerator.interrupt();
            orderGenerator.join();

        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
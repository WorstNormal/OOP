package ru.nsu.gaev;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ru.nsu.gaev.config.PizzeriaConfig;
import ru.nsu.gaev.model.Order;
import ru.nsu.gaev.model.OrderState;
import ru.nsu.gaev.staff.Baker;
import ru.nsu.gaev.staff.Courier;
import ru.nsu.gaev.storage.SharedQueue;
import ru.nsu.gaev.storage.Storage;

/**
 * Главный класс пиццерии - управляет производственным процессом.
 * Стратегия завершения: прекращение приема заказов, все оставшиеся заказы
 * в очереди и на складе сериализуются в файл для завершения на следующий день.
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
     * Создает новую пиццерию с заданной конфигурацией.
     *
     * @param config конфигурация пиццерии
     */
    public Pizzeria(PizzeriaConfig config) {
        this.config = config;
        this.orderQueue = new SharedQueue<>();
        this.storage = new Storage(config.getStorageCapacity());
    }

    /**
     * Запускает пиццерию: создает потоки пекарей и курьеров.
     */
    public void start() {
        System.out.println("=== Пиццерия открыта! ===");
        System.out.println("Вместимость склада: " + config.getStorageCapacity() + " пицц");
        System.out.println("Пекари: " + config.getBakers().length);
        System.out.println("Курьеры: " + config.getCouriers().length);
        System.out.println("Время работы: " + config.getWorkingTimeMs() + " мс");
        System.out.println("=========================\n");

        // Создание и запуск потоков пекарей
        for (PizzeriaConfig.BakerConfig bc : config.getBakers()) {
            Baker baker = new Baker(bc.getId(), bc.getCookingTimeMs(),
                    orderQueue, storage, interruptedOrders);
            Thread t = new Thread(baker, "Baker-" + bc.getId());
            t.setDaemon(true);
            bakerThreads.add(t);
            t.start();
        }

        // Создание и запуск потоков курьеров
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
     * Размещает новый заказ в очереди.
     *
     * @return созданный заказ или null, если заказы не принимаются
     */
    public Order placeOrder() {
        if (!acceptingOrders) {
            System.out.println("Заказы больше не принимаются!");
            return null;
        }
        int id = orderCounter.incrementAndGet();
        Order order = new Order(id);
        order.setState(OrderState.QUEUED);
        try {
            if (!orderQueue.put(order)) {
                System.out.println("Не удалось разместить заказ #" + id + " - очередь закрыта.");
                return null;
            }
        } catch (InterruptedException e) {
            System.out.println("Не удалось разместить заказ #" + id + " - прервано.");
            Thread.currentThread().interrupt();
            return null;
        }
        return order;
    }

    /**
     * Закрывает пиццерию.
     * 1. Прекращает прием новых заказов.
     * 2. Закрывает очередь заказов.
     * 3. Прерывает работу пекарей (стратегия немедленной остановки).
     * 4. Закрывает склад.
     * 5. Прерывает работу курьеров.
     * 6. Сериализует незавершенные заказы.
     */
    public void shutdown() {
        System.out.println("\n=== Пиццерия закрывается! ===");

        // 1. Прекратить прием заказов
        acceptingOrders = false;

        // 2. Закрыть очередь
        orderQueue.close();

        // 3. Прервать пекарей и дождаться их завершения
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

        // 4. Закрыть склад
        storage.close();

        // 5. Прервать курьеров и дождаться их завершения
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

        // 6. Сериализовать незавершенные заказы (если остались в очереди/на складе)
        List<Order> unfinishedFromQueue = orderQueue.drainAll();
        List<Order> unfinishedFromStorage = storage.drainAll();
        List<Order> allUnfinished = new ArrayList<>();
        allUnfinished.addAll(unfinishedFromQueue);
        allUnfinished.addAll(unfinishedFromStorage);
        allUnfinished.addAll(interruptedOrders);

        if (!allUnfinished.isEmpty()) {
            serializeUnfinished(allUnfinished);
        }

        System.out.println("=== Пиццерия закрыта! ===");
    }

    /**
     * Сериализует незавершенные заказы в JSON файл.
     *
     * @param orders список незавершенных заказов
     */
    private void serializeUnfinished(List<Order> orders) {
        System.out.println("Незавершенные заказы: " + orders.size());
        Gson gson = new Gson();
        List<UnfinishedOrder> data = new ArrayList<>();
        for (Order o : orders) {
            data.add(new UnfinishedOrder(o.getId(), o.getState().name()));
        }
        try (FileWriter writer = new FileWriter("unfinished_orders.json")) {
            gson.toJson(data, writer);
            System.out.println("Незавершенные заказы сохранены в unfinished_orders.json");
        } catch (IOException e) {
            System.err.println("Ошибка сериализации незавершенных заказов: " + e.getMessage());
        }
    }

    /**
     * Проверяет, принимает ли пиццерия заказы.
     *
     * @return true, если принимает заказы
     */
    public boolean isAcceptingOrders() {
        return acceptingOrders;
    }

    /**
     * Загружает конфигурацию пиццерии из JSON файла.
     *
     * @param path путь к конфигурационному файлу
     * @return загруженная конфигурация
     * @throws IOException если файл не может быть прочитан
     */
    public static PizzeriaConfig loadConfig(String path) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(path)) {
            return gson.fromJson(reader, PizzeriaConfig.class);
        }
    }

    /**
     * Вспомогательный класс для сериализации незавершенных заказов.
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

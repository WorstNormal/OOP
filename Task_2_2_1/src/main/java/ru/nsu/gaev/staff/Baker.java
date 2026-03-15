package ru.nsu.gaev.staff;

import ru.nsu.gaev.model.Order;
import ru.nsu.gaev.model.OrderState;
import ru.nsu.gaev.storage.SharedQueue;
import ru.nsu.gaev.storage.Storage;

import java.util.List;

/**
 * Пекарь - берет заказы из очереди, готовит пиццу и кладет ее на склад.
 */
public class Baker implements Runnable {
    private final int id;
    private final int cookingTimeMs;
    private final SharedQueue<Order> orderQueue;
    private final Storage storage;
    private final List<Order> interruptedOrders;

    /**
     * Создает нового пекаря.
     *
     * @param id                идентификатор пекаря
     * @param cookingTimeMs     время приготовления одной пиццы (мс)
     * @param orderQueue        общая очередь заказов
     * @param storage           склад готовой продукции
     * @param interruptedOrders список для сохранения прерванных заказов
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
                    // Очередь закрыта и пуста - прекращаем работу
                    break;
                }

                // Начало приготовления
                currentOrder.setState(OrderState.COOKING);
                Thread.sleep(cookingTimeMs);

                currentOrder.setState(OrderState.COOKED);

                // Попытка положить на склад (ожидание, если склад полон)
                if (!storage.put(currentOrder)) {
                    // Склад закрыт - сохраняем заказ
                    interruptedOrders.add(currentOrder);
                    break;
                }
                currentOrder = null; // Заказ успешно обработан
            } catch (InterruptedException e) {
                System.out.println("Пекарь #" + id + " прерван.");
                if (currentOrder != null) {
                    interruptedOrders.add(currentOrder);
                }
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Пекарь #" + id + " закончил работу.");
    }

    /**
     * Возвращает идентификатор пекаря.
     *
     * @return id пекаря
     */
    public int getBakerId() {
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

package ru.nsu.gaev.storage;

import java.util.List;
import ru.nsu.gaev.model.Order;
import ru.nsu.gaev.model.OrderState;

/**
 * Склад готовой продукции вместимостью T пицц.
 * Обертка над SharedQueue с ограниченной вместимостью.
 */
public class Storage {
    private final SharedQueue<Order> queue;
    private final int capacity;

    /**
     * Создает склад с заданной вместимостью.
     *
     * @param capacity максимальное количество пицц, которое можно хранить
     */
    public Storage(int capacity) {
        this.capacity = capacity;
        this.queue = new SharedQueue<>(capacity);
    }

    /**
     * Кладет пиццу на склад. Блокирует поток, если склад полон.
     *
     * @param order готовый заказ
     * @return true, если успешно, false, если склад закрыт
     * @throws InterruptedException если текущий поток прерван во время ожидания
     */
    public boolean put(Order order) throws InterruptedException {
        boolean result = queue.put(order);
        if (result) {
            order.setState(OrderState.IN_STORAGE);
        }
        return result;
    }

    /**
     * Берет до maxCount пицц со склада. Блокирует поток, если склад пуст.
     *
     * @param maxCount максимальное количество пицц (вместимость багажника)
     * @return список заказов
     * @throws InterruptedException если текущий поток прерван во время ожидания
     */
    public List<Order> takeUpTo(int maxCount) throws InterruptedException {
        return queue.takeUpTo(maxCount);
    }

    /**
     * Закрывает склад (будит все ожидающие потоки).
     */
    public void close() {
        queue.close();
    }

    /**
     * Проверяет, закрыт ли склад и пуст ли он.
     *
     * @return true, если закрыт и пуст
     */
    public boolean isClosedAndEmpty() {
        return queue.isClosedAndEmpty();
    }

    /**
     * Проверяет, закрыт ли склад.
     *
     * @return true, если закрыт
     */
    public boolean isClosed() {
        return queue.isClosed();
    }

    /**
     * Возвращает текущее количество пицц на складе.
     *
     * @return текущий размер
     */
    public int size() {
        return queue.size();
    }

    /**
     * Возвращает вместимость склада.
     *
     * @return вместимость
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Получает все оставшиеся заказы на складе (для сериализации).
     *
     * @return список всех оставшихся заказов
     */
    public List<Order> drainAll() {
        return queue.drainAll();
    }
}

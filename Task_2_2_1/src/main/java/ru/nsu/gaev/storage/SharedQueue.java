package ru.nsu.gaev.storage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Потокобезопасная очередь, основанная на LinkedList и синхронизации.
 * Реализована без использования java.util.concurrent.BlockingQueue.
 *
 * @param <T> тип элементов очереди
 */
public class SharedQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    private final int capacity;
    private boolean closed = false;

    /**
     * Создает очередь с заданной максимальной вместимостью.
     *
     * @param capacity максимальная вместимость (0 означает без ограничений)
     */
    public SharedQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Создает очередь с неограниченной вместимостью.
     */
    public SharedQueue() {
        this(0);
    }

    /**
     * Добавляет элемент в очередь. Если очередь полна, блокирует поток
     * до тех пор, пока не появится место или очередь не будет закрыта.
     *
     * @param item элемент для добавления
     * @return true, если элемент добавлен, false, если очередь закрыта
     * @throws InterruptedException если текущий поток прерван во время ожидания
     */
    public synchronized boolean put(T item) throws InterruptedException {
        while (capacity > 0 && queue.size() >= capacity && !closed) {
            wait();
        }
        if (closed) {
            return false;
        }
        queue.addLast(item);
        notifyAll();
        return true;
    }

    /**
     * Удаляет и возвращает элемент из очереди. Если очередь пуста,
     * блокирует поток до тех пор, пока элемент не станет доступен или очередь не будет закрыта.
     *
     * @return элемент или null, если очередь закрыта и пуста
     * @throws InterruptedException если текущий поток прерван во время ожидания
     */
    public synchronized T take() throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
        }
        if (queue.isEmpty()) {
            return null;
        }
        T item = queue.removeFirst();
        notifyAll();
        return item;
    }

    /**
     * Удаляет и возвращает до maxCount элементов из очереди. Если очередь
     * пуста, блокирует поток до тех пор, пока не станет доступен хотя бы один элемент
     * или очередь не будет закрыта.
     *
     * @param maxCount максимальное количество удаляемых элементов
     * @return список элементов (может быть пустым, если очередь закрыта)
     * @throws InterruptedException если текущий поток прерван во время ожидания
     */
    public synchronized List<T> takeUpTo(int maxCount) throws InterruptedException {
        while (queue.isEmpty() && !closed) {
            wait();
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
     * Закрывает очередь. Все ожидающие потоки будут разбужены.
     */
    public synchronized void close() {
        closed = true;
        notifyAll();
    }

    /**
     * Проверяет, закрыта ли очередь.
     *
     * @return true, если закрыта
     */
    public synchronized boolean isClosed() {
        return closed;
    }

    /**
     * Проверяет, закрыта ли очередь и пуста ли она.
     *
     * @return true, если закрыта и пуста
     */
    public synchronized boolean isClosedAndEmpty() {
        return closed && queue.isEmpty();
    }

    /**
     * Возвращает текущий размер очереди.
     *
     * @return текущий размер
     */
    public synchronized int size() {
        return queue.size();
    }

    /**
     * Возвращает все оставшиеся элементы (для сериализации незавершенных заказов).
     *
     * @return список всех оставшихся элементов
     */
    public synchronized List<T> drainAll() {
        List<T> result = new ArrayList<>(queue);
        if (!queue.isEmpty()) {
            queue.clear();
            notifyAll();
        }
        return result;
    }
}

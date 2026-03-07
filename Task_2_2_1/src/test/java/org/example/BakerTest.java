package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class BakerTest {

    @Test
    void testBakerProperties() {
        SharedQueue<Order> queue = new SharedQueue<>();
        Storage storage = new Storage(10);
        Baker baker = new Baker(1, 100, queue, storage, new ArrayList<>());

        assertEquals(1, baker.getBakerId());
        assertEquals(100, baker.getCookingTimeMs());
    }

    @Test
    @Timeout(5)
    void testBakerProcessesOrder() throws InterruptedException {
        SharedQueue<Order> queue = new SharedQueue<>();
        Storage storage = new Storage(10);
        Baker baker = new Baker(1, 50, queue, storage, new ArrayList<>());

        Order order = new Order(1);
        queue.put(order);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        // Даём пекарю время обработать заказ
        Thread.sleep(200);

        queue.close();
        bakerThread.join(1000);

        assertEquals(OrderState.IN_STORAGE, order.getState());
        assertEquals(1, storage.size());
    }

    @Test
    @Timeout(5)
    void testBakerStopsOnClosedQueue() throws InterruptedException {
        SharedQueue<Order> queue = new SharedQueue<>();
        Storage storage = new Storage(10);
        Baker baker = new Baker(1, 50, queue, storage, new ArrayList<>());

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(100);
        queue.close();
        bakerThread.join(1000);

        assertFalse(bakerThread.isAlive());
    }

    @Test
    @Timeout(5)
    void testBakerStopsOnClosedStorage() throws InterruptedException {
        SharedQueue<Order> queue = new SharedQueue<>();
        Storage storage = new Storage(1);
        Baker baker = new Baker(1, 10, queue, storage, new ArrayList<>());

        // Заполняем склад
        storage.put(new Order(100));

        Order order = new Order(1);
        queue.put(order);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        // Даём время начать готовить
        Thread.sleep(50);

        // Закрываем склад пока пекарь ждёт
        storage.close();
        queue.close();

        bakerThread.join(2000);
        assertFalse(bakerThread.isAlive());
    }

    @Test
    @Timeout(5)
    void testBakerInterrupted() throws InterruptedException {
        SharedQueue<Order> queue = new SharedQueue<>();
        Storage storage = new Storage(10);
        List<Order> interrupted = new ArrayList<>();
        Baker baker = new Baker(1, 5000, queue, storage, interrupted);

        Order order = new Order(1);
        queue.put(order);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(100);
        bakerThread.interrupt();
        bakerThread.join(1000);

        assertFalse(bakerThread.isAlive());
    }

    @Test
    @Timeout(5)
    void testBakerMultipleOrders() throws InterruptedException {
        SharedQueue<Order> queue = new SharedQueue<>();
        Storage storage = new Storage(10);
        Baker baker = new Baker(1, 20, queue, storage, new ArrayList<>());

        Order order1 = new Order(1);
        Order order2 = new Order(2);
        Order order3 = new Order(3);

        queue.put(order1);
        queue.put(order2);
        queue.put(order3);

        Thread bakerThread = new Thread(baker);
        bakerThread.start();

        Thread.sleep(300);
        queue.close();
        bakerThread.join(1000);

        assertEquals(OrderState.IN_STORAGE, order1.getState());
        assertEquals(OrderState.IN_STORAGE, order2.getState());
        assertEquals(OrderState.IN_STORAGE, order3.getState());
        assertEquals(3, storage.size());
    }
}

package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SharedQueueTest {

    @Test
    void testPutAndTake() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        assertTrue(queue.put(1));
        assertTrue(queue.put(2));
        assertEquals(2, queue.size());

        assertEquals(1, queue.take());
        assertEquals(2, queue.take());
        assertEquals(0, queue.size());
    }

    @Test
    void testQueueWithCapacity() {
        SharedQueue<Integer> queue = new SharedQueue<>(2);
        assertTrue(queue.put(1));
        assertTrue(queue.put(2));
        assertEquals(2, queue.size());
    }

    @Test
    void testClose() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        assertFalse(queue.isClosed());
        queue.close();
        assertTrue(queue.isClosed());
    }

    @Test
    void testIsClosedAndEmpty() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.put(1);
        assertFalse(queue.isClosedAndEmpty());

        queue.close();
        assertFalse(queue.isClosedAndEmpty());

        queue.take();
        assertTrue(queue.isClosedAndEmpty());
    }

    @Test
    void testPutOnClosedQueue() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.close();
        assertFalse(queue.put(1));
    }

    @Test
    void testTakeFromClosedEmptyQueue() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.close();
        assertNull(queue.take());
    }

    @Test
    void testTakeFromClosedNonEmptyQueue() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.put(1);
        queue.close();
        assertEquals(1, queue.take());
        assertNull(queue.take());
    }

    @Test
    void testTakeUpTo() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.put(1);
        queue.put(2);
        queue.put(3);

        List<Integer> items = queue.takeUpTo(2);
        assertEquals(2, items.size());
        assertEquals(1, items.get(0));
        assertEquals(2, items.get(1));
        assertEquals(1, queue.size());
    }

    @Test
    void testTakeUpToMoreThanAvailable() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.put(1);
        queue.put(2);

        List<Integer> items = queue.takeUpTo(10);
        assertEquals(2, items.size());
        assertEquals(0, queue.size());
    }

    @Test
    void testTakeUpToFromClosedEmptyQueue() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.close();
        List<Integer> items = queue.takeUpTo(5);
        assertTrue(items.isEmpty());
    }

    @Test
    void testDrainAll() {
        SharedQueue<Integer> queue = new SharedQueue<>();
        queue.put(1);
        queue.put(2);
        queue.put(3);

        List<Integer> items = queue.drainAll();
        assertEquals(3, items.size());
        assertEquals(0, queue.size());
    }

    @Test
    @Timeout(5)
    void testBlockingTake() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>();
        AtomicReference<Integer> result = new AtomicReference<>();
        CountDownLatch started = new CountDownLatch(1);

        Thread consumer = new Thread(() -> {
            started.countDown();
            result.set(queue.take());
        });
        consumer.start();

        started.await();
        Thread.sleep(100); // Даём потоку время заблокироваться

        queue.put(42);
        consumer.join(1000);

        assertEquals(42, result.get());
    }

    @Test
    @Timeout(5)
    void testBlockingTakeWakesUpOnClose() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>();
        AtomicReference<Integer> result = new AtomicReference<>(999);
        CountDownLatch started = new CountDownLatch(1);

        Thread consumer = new Thread(() -> {
            started.countDown();
            result.set(queue.take());
        });
        consumer.start();

        started.await();
        Thread.sleep(100);

        queue.close();
        consumer.join(1000);

        assertNull(result.get());
    }

    @Test
    @Timeout(5)
    void testBlockingPutOnFullQueue() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>(1);
        queue.put(1);

        AtomicBoolean added = new AtomicBoolean(false);
        CountDownLatch started = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            started.countDown();
            added.set(queue.put(2));
        });
        producer.start();

        started.await();
        Thread.sleep(100); // Поток заблокирован

        queue.take(); // Освобождаем место
        producer.join(1000);

        assertTrue(added.get());
    }

    @Test
    @Timeout(5)
    void testBlockingPutWakesUpOnClose() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>(1);
        queue.put(1);

        AtomicBoolean added = new AtomicBoolean(true);
        CountDownLatch started = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            started.countDown();
            added.set(queue.put(2));
        });
        producer.start();

        started.await();
        Thread.sleep(100);

        queue.close();
        producer.join(1000);

        assertFalse(added.get());
    }

    @Test
    @Timeout(5)
    void testTakeUpToBlocksUntilItemAvailable() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>();
        AtomicReference<List<Integer>> result = new AtomicReference<>();
        CountDownLatch started = new CountDownLatch(1);

        Thread consumer = new Thread(() -> {
            started.countDown();
            result.set(queue.takeUpTo(3));
        });
        consumer.start();

        started.await();
        Thread.sleep(100);

        queue.put(1);
        queue.put(2);
        consumer.join(1000);

        assertNotNull(result.get());
        assertEquals(2, result.get().size());
    }

    @Test
    @Timeout(5)
    void testInterruptedWhilePut() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>(1);
        queue.put(1);

        Thread producer = new Thread(() -> {
            queue.put(2);
        });
        producer.start();

        Thread.sleep(100);
        producer.interrupt();
        producer.join(1000);

        assertTrue(producer.isInterrupted() || !producer.isAlive());
    }

    @Test
    @Timeout(5)
    void testInterruptedWhileTake() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>();

        Thread consumer = new Thread(() -> {
            queue.take();
        });
        consumer.start();

        Thread.sleep(100);
        consumer.interrupt();
        consumer.join(1000);

        assertFalse(consumer.isAlive());
    }

    @Test
    @Timeout(5)
    void testInterruptedWhileTakeUpTo() throws InterruptedException {
        SharedQueue<Integer> queue = new SharedQueue<>();

        Thread consumer = new Thread(() -> {
            queue.takeUpTo(5);
        });
        consumer.start();

        Thread.sleep(100);
        consumer.interrupt();
        consumer.join(1000);

        assertFalse(consumer.isAlive());
    }
}


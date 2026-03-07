package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    @Test
    void testPutAndTakeUpTo() {
        Storage storage = new Storage(5);
        Order order1 = new Order(1);
        Order order2 = new Order(2);

        assertTrue(storage.put(order1));
        assertTrue(storage.put(order2));
        assertEquals(2, storage.size());

        // После put состояние меняется на IN_STORAGE
        assertEquals(OrderState.IN_STORAGE, order1.getState());
        assertEquals(OrderState.IN_STORAGE, order2.getState());

        List<Order> orders = storage.takeUpTo(1);
        assertEquals(1, orders.size());
        assertEquals(1, storage.size());

        orders = storage.takeUpTo(5);
        assertEquals(1, orders.size());
        assertEquals(0, storage.size());
    }

    @Test
    void testGetCapacity() {
        Storage storage = new Storage(10);
        assertEquals(10, storage.getCapacity());
    }

    @Test
    void testClose() {
        Storage storage = new Storage(5);
        assertFalse(storage.isClosed());
        storage.close();
        assertTrue(storage.isClosed());
    }

    @Test
    void testIsClosedAndEmpty() {
        Storage storage = new Storage(5);
        Order order = new Order(1);
        storage.put(order);

        assertFalse(storage.isClosedAndEmpty());
        storage.close();
        assertFalse(storage.isClosedAndEmpty());

        storage.takeUpTo(1);
        assertTrue(storage.isClosedAndEmpty());
    }

    @Test
    void testDrainAll() {
        Storage storage = new Storage(5);
        storage.put(new Order(1));
        storage.put(new Order(2));
        storage.put(new Order(3));

        List<Order> orders = storage.drainAll();
        assertEquals(3, orders.size());
        assertEquals(0, storage.size());
    }

    @Test
    @Timeout(5)
    void testPutOnClosedStorage() {
        Storage storage = new Storage(5);
        storage.close();
        Order order = new Order(1);
        assertFalse(storage.put(order));
        // Состояние не должно измениться на IN_STORAGE
        assertNotEquals(OrderState.IN_STORAGE, order.getState());
    }

    @Test
    @Timeout(5)
    void testTakeUpToFromClosedEmptyStorage() {
        Storage storage = new Storage(5);
        storage.close();
        List<Order> orders = storage.takeUpTo(3);
        assertTrue(orders.isEmpty());
    }
}


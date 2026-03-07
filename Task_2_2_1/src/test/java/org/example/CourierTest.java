package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourierTest {

    @Test
    void testCourierProperties() {
        Storage storage = new Storage(10);
        Courier courier = new Courier(1, 3, 100, storage, new ArrayList<>());

        assertEquals(1, courier.getCourierId());
        assertEquals(3, courier.getTrunkCapacity());
        assertEquals(100, courier.getDeliveryTimeMs());
    }

    @Test
    @Timeout(5)
    void testCourierDeliversOrder() throws InterruptedException {
        Storage storage = new Storage(10);
        Courier courier = new Courier(1, 3, 50, storage, new ArrayList<>());

        Order order = new Order(1);
        order.setState(OrderState.IN_STORAGE);
        storage.put(order);

        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(200);
        storage.close();
        courierThread.join(1000);

        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    @Timeout(5)
    void testCourierStopsOnClosedEmptyStorage() throws InterruptedException {
        Storage storage = new Storage(10);
        Courier courier = new Courier(1, 3, 50, storage, new ArrayList<>());

        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(100);
        storage.close();
        courierThread.join(1000);

        assertFalse(courierThread.isAlive());
    }

    @Test
    @Timeout(5)
    void testCourierTakesMultipleOrders() throws InterruptedException {
        Storage storage = new Storage(10);
        Courier courier = new Courier(1, 3, 50, storage, new ArrayList<>());

        Order order1 = new Order(1);
        Order order2 = new Order(2);
        Order order3 = new Order(3);

        storage.put(order1);
        storage.put(order2);
        storage.put(order3);

        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(200);
        storage.close();
        courierThread.join(1000);

        assertEquals(OrderState.DELIVERED, order1.getState());
        assertEquals(OrderState.DELIVERED, order2.getState());
        assertEquals(OrderState.DELIVERED, order3.getState());
    }

    @Test
    @Timeout(5)
    void testCourierTrunkCapacityLimit() throws InterruptedException {
        Storage storage = new Storage(10);
        // Курьер может взять только 2 пиццы за раз
        Courier courier = new Courier(1, 2, 50, storage, new ArrayList<>());

        Order order1 = new Order(1);
        Order order2 = new Order(2);
        Order order3 = new Order(3);

        storage.put(order1);
        storage.put(order2);
        storage.put(order3);

        Thread courierThread = new Thread(courier);
        courierThread.start();

        // Первая доставка - 2 заказа
        Thread.sleep(150);

        // После первой доставки должен остаться 1 заказ
        storage.close();
        courierThread.join(1000);

        assertEquals(OrderState.DELIVERED, order1.getState());
        assertEquals(OrderState.DELIVERED, order2.getState());
        assertEquals(OrderState.DELIVERED, order3.getState());
    }

    @Test
    @Timeout(5)
    void testCourierInterrupted() throws InterruptedException {
        Storage storage = new Storage(10);
        Courier courier = new Courier(1, 3, 5000, storage, new ArrayList<>());

        Order order = new Order(1);
        storage.put(order);

        Thread courierThread = new Thread(courier);
        courierThread.start();

        Thread.sleep(100);
        courierThread.interrupt();
        courierThread.join(1000);

        assertFalse(courierThread.isAlive());
    }
}

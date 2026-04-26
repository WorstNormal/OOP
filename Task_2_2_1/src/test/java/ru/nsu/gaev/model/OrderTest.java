package ru.nsu.gaev.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void testOrderCreation() {
        Order order = new Order(1);
        assertEquals(1, order.getId());
        assertEquals(OrderState.QUEUED, order.getState());
    }

    @Test
    void testSetState() {
        Order order = new Order(1);
        order.setState(OrderState.COOKING);
        assertEquals(OrderState.COOKING, order.getState());

        order.setState(OrderState.COOKED);
        assertEquals(OrderState.COOKED, order.getState());

        order.setState(OrderState.DELIVERED);
        assertEquals(OrderState.DELIVERED, order.getState());
    }

    @Test
    void testToString() {
        Order order = new Order(5);
        String str = order.toString();
        assertNotNull(str);
        // Проверка что строка содержит ID и состояние
        assertTrue(str.contains("5"));
        assertTrue(str.contains(OrderState.QUEUED.toString()));
    }
}

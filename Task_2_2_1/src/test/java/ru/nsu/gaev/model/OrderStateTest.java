package ru.nsu.gaev.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class OrderStateTest {

    @Test
    void testGetDescription() {
        assertEquals("В очереди", OrderState.QUEUED.getDescription());
        assertEquals("Готовится", OrderState.COOKING.getDescription());
        assertEquals("Приготовлено", OrderState.COOKED.getDescription());
        assertEquals("На складе", OrderState.IN_STORAGE.getDescription());
        assertEquals("Доставляется", OrderState.DELIVERING.getDescription());
        assertEquals("Доставлено", OrderState.DELIVERED.getDescription());
    }

    @Test
    void testToString() {
        assertEquals("В очереди", OrderState.QUEUED.toString());
        assertEquals("Готовится", OrderState.COOKING.toString());
        assertEquals("Приготовлено", OrderState.COOKED.toString());
        assertEquals("На складе", OrderState.IN_STORAGE.toString());
        assertEquals("Доставляется", OrderState.DELIVERING.toString());
        assertEquals("Доставлено", OrderState.DELIVERED.toString());
    }

    @Test
    void testAllEnumValues() {
        OrderState[] values = OrderState.values();
        assertEquals(6, values.length);
        assertNotNull(OrderState.valueOf("QUEUED"));
        assertNotNull(OrderState.valueOf("COOKING"));
        assertNotNull(OrderState.valueOf("COOKED"));
        assertNotNull(OrderState.valueOf("IN_STORAGE"));
        assertNotNull(OrderState.valueOf("DELIVERING"));
        assertNotNull(OrderState.valueOf("DELIVERED"));
    }
}

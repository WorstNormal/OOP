package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStateTest {

    @Test
    void testGetDescription() {
        assertEquals("Queued", OrderState.QUEUED.getDescription());
        assertEquals("Cooking", OrderState.COOKING.getDescription());
        assertEquals("Cooked", OrderState.COOKED.getDescription());
        assertEquals("In Storage", OrderState.IN_STORAGE.getDescription());
        assertEquals("Delivering", OrderState.DELIVERING.getDescription());
        assertEquals("Delivered", OrderState.DELIVERED.getDescription());
    }

    @Test
    void testToString() {
        assertEquals("Queued", OrderState.QUEUED.toString());
        assertEquals("Cooking", OrderState.COOKING.toString());
        assertEquals("Cooked", OrderState.COOKED.toString());
        assertEquals("In Storage", OrderState.IN_STORAGE.toString());
        assertEquals("Delivering", OrderState.DELIVERING.toString());
        assertEquals("Delivered", OrderState.DELIVERED.toString());
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

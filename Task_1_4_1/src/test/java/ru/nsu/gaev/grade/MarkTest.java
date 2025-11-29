package ru.nsu.gaev.grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for Mark enum.
 */
public class MarkTest {

    @Test
    public void testMarkFromValueValid() {
        assertEquals(Mark.SATISFACTORY, Mark.fromValue(2));
        assertEquals(Mark.GOOD, Mark.fromValue(3));
        assertEquals(Mark.EXCELLENT, Mark.fromValue(4));
        assertEquals(Mark.EXCELLENT_PLUS, Mark.fromValue(5));
    }

    @Test
    public void testMarkFromValueInvalidLow() {
        assertThrows(IllegalArgumentException.class, () -> Mark.fromValue(1));
    }

    @Test
    public void testMarkFromValueInvalidHigh() {
        assertThrows(IllegalArgumentException.class, () -> Mark.fromValue(6));
    }

    @Test
    public void testGetters() {
        assertEquals(2, Mark.SATISFACTORY.getValue());
        assertNotNull(Mark.EXCELLENT_PLUS.getDisplayName());
    }
}

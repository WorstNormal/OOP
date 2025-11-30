package ru.nsu.gaev.grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GradeEnumsTest {

    @Test
    void testMarkValues() {
        assertEquals(5, Mark.EXCELLENT.getValue());
        assertEquals(2, Mark.BAD.getValue());
    }

    @Test
    void testMarkFromValue() {
        assertEquals(Mark.EXCELLENT, Mark.fromValue(5));
        assertEquals(Mark.SATISFACTORY, Mark.fromValue(3));
    }

    @Test
    void testMarkFromValueInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Mark.fromValue(1));
        assertThrows(IllegalArgumentException.class, () -> Mark.fromValue(6));
    }

    @Test
    void testCreditStatus() {
        assertTrue(CreditStatus.PASSED.isPassed());
        assertFalse(CreditStatus.NOT_PASSED.isPassed());
        assertEquals("Зачет", CreditStatus.PASSED.getDisplayName());
    }
}
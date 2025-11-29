package ru.nsu.gaev.grade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Тесты для enum CreditStatus (статусы зачетов).
 */
public class CreditStatusTest {

    @Test
    public void testCreditStatusPassed() {
        assertEquals("Зачет", CreditStatus.PASSED.getDisplayName());
        assertTrue(CreditStatus.PASSED.isPassed());
    }

    @Test
    public void testCreditStatusNotPassed() {
        assertEquals("Не зачет", CreditStatus.NOT_PASSED.getDisplayName());
        assertFalse(CreditStatus.NOT_PASSED.isPassed());
    }
}

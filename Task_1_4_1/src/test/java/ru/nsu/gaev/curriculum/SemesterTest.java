package ru.nsu.gaev.curriculum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Тесты для класса Semester.
 */
public class SemesterTest {

    @Test
    public void testSemesterCreation() {
        Semester semester = new Semester(1);
        assertEquals(1, semester.getSemesterNumber());
    }

    @Test
    public void testSemesterEquality() {
        Semester semester1 = new Semester(1);
        Semester semester1Copy = new Semester(1);
        Semester semester2 = new Semester(2);

        assertEquals(semester1, semester1Copy);
        assertEquals(semester1.hashCode(), semester1Copy.hashCode());
        assertThrows(AssertionError.class, () -> {
            assertEquals(semester1, semester2);
        });
    }

    @Test
    public void testSemesterInvalidNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new Semester(0));
        assertThrows(IllegalArgumentException.class,
                () -> new Semester(-1));
    }

    @Test
    public void testSemesterToString() {
        Semester semester = new Semester(1);
        assertEquals("Семестр 1", semester.toString());
    }
}

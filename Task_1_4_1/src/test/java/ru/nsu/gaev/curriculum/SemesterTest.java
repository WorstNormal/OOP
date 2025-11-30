package ru.nsu.gaev.curriculum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class SemesterTest {

    @Test
    void testConstructorAndStaticFactory() {
        Semester s = new Semester(1);
        assertEquals(1, s.getSemesterNumber());
        assertEquals(1, s.getCourseNumber());

        assertEquals(new Semester(1), Semester.of(1, 1));
        assertEquals(new Semester(2), Semester.of(1, 2));
        assertEquals(new Semester(3), Semester.of(2, 1));
    }

    @Test
    void testInvalidConstruction() {
        assertThrows(IllegalArgumentException.class, () -> new Semester(0));
        assertThrows(IllegalArgumentException.class, () -> Semester.of(0, 1));
        assertThrows(IllegalArgumentException.class, () -> Semester.of(1, 3));
    }

    @Test
    void testNavigation() {
        Semester s1 = new Semester(1);
        assertEquals(new Semester(2), s1.next());

        assertFalse(s1.previous().isPresent());
        assertEquals(s1, new Semester(2).previous().orElse(null));
    }

    @Test
    void testProperties() {
        Semester s1 = new Semester(1);
        Semester s2 = new Semester(2);

        assertTrue(s1.isOdd());
        assertFalse(s1.isEven());
        assertTrue(s2.isEven());
        assertFalse(s2.isOdd());
    }

    @Test
    void testEqualsAndHashCode() {
        Semester s1 = new Semester(5);
        Semester s1Copy = new Semester(5);
        Semester s2 = new Semester(6);

        assertEquals(s1, s1);
        assertEquals(s1, s1Copy);
        assertEquals(s1.hashCode(), s1Copy.hashCode());

        assertNotEquals(s1, s2);
        assertNotEquals(s1, null);
        assertNotEquals(s1, new Object());
    }

    @Test
    void testCompareTo() {
        Semester s1 = new Semester(1);
        Semester s2 = new Semester(2);
        Semester s1Copy = new Semester(1);

        assertTrue(s1.compareTo(s2) < 0);
        assertTrue(s2.compareTo(s1) > 0);
        assertEquals(0, s1.compareTo(s1Copy));
    }

    @Test
    void testToString() {
        assertNotNull(new Semester(1).toString());
    }
}
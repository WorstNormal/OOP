package ru.nsu.gaev.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.curriculum.Semester;

class StudentTest {

    @Test
    void testConstructorAndGetters() {
        Semester s1 = new Semester(1);
        ElectronicGradeBook book = new ElectronicGradeBook(true, s1);
        Student student = new Student("Ivanov", "123", book);

        assertEquals("Ivanov", student.getFullName());
        assertEquals("123", student.getStudentId());
        assertEquals(book, student.getGradeBook());
    }

    @Test
    void testEqualsAndHashCode() {
        // Равенство определяется только по studentId
        Semester s1 = new Semester(1);
        ElectronicGradeBook book = new ElectronicGradeBook(true, s1);

        Student st1 = new Student("Ivanov", "123", book);
        Student st2 = new Student("Petrov", "123", book); // То же ID
        Student st3 = new Student("Ivanov", "999", book); // Другой ID

        assertEquals(st1, st2);
        assertEquals(st1.hashCode(), st2.hashCode());

        assertNotEquals(st1, st3);
        assertNotEquals(st1, null);
        assertNotEquals(st1, new Object());
    }

    @Test
    void testToString() {
        Student student = new Student("Ivanov", "123", null);
        String str = student.toString();

        assertNotNull(str);
        // Простая проверка формата: "Ivanov (123)"
        assertEquals("Ivanov (123)", str);
    }
}
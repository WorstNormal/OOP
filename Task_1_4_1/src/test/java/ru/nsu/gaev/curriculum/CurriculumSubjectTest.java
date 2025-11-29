package ru.nsu.gaev.curriculum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса CurriculumSubject.
 */
public class CurriculumSubjectTest {

    private static final String SUBJECT_NAME = "Математический анализ";

    private Semester semester1;
    private Semester semester2;
    private CurriculumSubject subject1;

    /**
     * Подготовка фикстур перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        semester1 = new Semester(1);
        semester2 = new Semester(2);
        subject1 = new CurriculumSubject(
                SUBJECT_NAME,
                semester1,
                ControlType.EXAM);
    }

    @Test
    public void testCurriculumSubjectCreation() {
        assertNotNull(subject1);
        assertEquals(SUBJECT_NAME, subject1.getSubjectName());
        assertEquals(semester1, subject1.getSemester());
        assertEquals(ControlType.EXAM, subject1.getControlType());
    }

    @Test
    public void testCurriculumSubjectEquality() {
        CurriculumSubject subject1Copy = new CurriculumSubject(
                SUBJECT_NAME,
                semester1,
                ControlType.EXAM);
        assertEquals(subject1, subject1Copy);
        assertEquals(subject1.hashCode(), subject1Copy.hashCode());
    }

    @Test
    public void testCurriculumSubjectToString() {
        String expected = SUBJECT_NAME + " (Экзамен) - "
                + "Семестр 1";
        assertEquals(expected, subject1.toString());
    }

    @Test
    public void testNotEqualsDifferentName() {
        CurriculumSubject other = new CurriculumSubject(
                "Физика",
                semester1,
                ControlType.EXAM);
        assertNotEquals(subject1, other);
    }

    @Test
    public void testNotEqualsDifferentSemester() {
        CurriculumSubject other = new CurriculumSubject(
                SUBJECT_NAME,
                semester2,
                ControlType.EXAM);
        assertNotEquals(subject1, other);
    }

    @Test
    public void testNotEqualsDifferentControlType() {
        CurriculumSubject other = new CurriculumSubject(
                SUBJECT_NAME,
                semester1,
                ControlType.CREDIT);
        assertNotEquals(subject1, other);
    }

    @Test
    public void testEqualsNullAndOtherType() {
        assertNotEquals(null, subject1);
        assertNotEquals("string", subject1);
    }
}

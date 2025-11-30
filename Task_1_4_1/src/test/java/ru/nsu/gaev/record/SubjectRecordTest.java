package ru.nsu.gaev.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;

class SubjectRecordTest {

    @Test
    void testConstructorWithMark() {
        Semester s1 = new Semester(1);
        SubjectRecord record = new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT);

        assertEquals("Math", record.getSubjectName());
        assertEquals(s1, record.getSemester());
        assertEquals(ControlType.EXAM, record.getControlType());
        assertEquals(Mark.EXCELLENT, record.getMark());
        assertEquals(5, record.getGradeValue());
    }

    @Test
    void testConstructorWithCredit() {
        Semester s1 = new Semester(1);
        SubjectRecord record = new SubjectRecord("PE", s1,
                ControlType.CREDIT, CreditStatus.PASSED);

        assertEquals(CreditStatus.PASSED, record.getCreditStatus());
        assertEquals(1, record.getGradeValue());
    }

    @Test
    void testConstructorWithCreditNotPassed() {
        Semester s1 = new Semester(1);
        SubjectRecord record = new SubjectRecord("PE", s1,
                ControlType.CREDIT, CreditStatus.NOT_PASSED);
        assertEquals(0, record.getGradeValue());
    }

    @Test
    void testConstructorInvalidType() {
        Semester s1 = new Semester(1);
        // Покрывает ветку if (controlType != ControlType.CREDIT)
        assertThrows(IllegalArgumentException.class, () ->
                new SubjectRecord("Math", s1, ControlType.EXAM, CreditStatus.PASSED));
    }

    @Test
    void testEqualsAndHashCode() {
        Semester s1 = new Semester(1);
        SubjectRecord record1 = new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT);
        SubjectRecord record2 = new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT);

        // 1. Рефлексивность (this == o)
        assertEquals(record1, record1);

        // 2. Симметричность
        assertEquals(record1, record2);
        assertEquals(record2, record1);
        assertEquals(record1.hashCode(), record2.hashCode());

        // 3. Неравенство (null и другой класс)
        assertNotEquals(record1, null);
        assertNotEquals(record1, "Some String");

        // 4. Неравенство по полям
        SubjectRecord diffNameRecord = new SubjectRecord("Java", s1,
                ControlType.EXAM, Mark.EXCELLENT);
        assertNotEquals(record1, diffNameRecord);

        SubjectRecord diffSemRecord = new SubjectRecord("Math", new Semester(2),
                ControlType.EXAM, Mark.EXCELLENT);
        assertNotEquals(record1, diffSemRecord);

        SubjectRecord diffTypeRecord = new SubjectRecord("Math", s1,
                ControlType.CREDIT, CreditStatus.PASSED);
        assertNotEquals(record1, diffTypeRecord);
    }

    @Test
    void testToString() {
        Semester s1 = new Semester(1);
        SubjectRecord markRecord = new SubjectRecord("Math", s1,
                ControlType.EXAM, Mark.EXCELLENT);
        assertNotNull(markRecord.toString());
        assertTrue(markRecord.toString().contains("Отлично"));

        SubjectRecord creditRecord = new SubjectRecord("PE", s1,
                ControlType.CREDIT, CreditStatus.PASSED);
        assertNotNull(creditRecord.toString());

        // Явно приводим null к типу Mark
        SubjectRecord emptyRecord = new SubjectRecord("Lost", s1, ControlType.EXAM, (Mark) null);
        assertNotNull(emptyRecord.toString());
    }
}
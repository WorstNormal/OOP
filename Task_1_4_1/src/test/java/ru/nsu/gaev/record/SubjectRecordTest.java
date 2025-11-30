package ru.nsu.gaev.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        SubjectRecord record = new SubjectRecord("PE", s1, ControlType.CREDIT, CreditStatus.PASSED);

        assertEquals(CreditStatus.PASSED, record.getCreditStatus());
        assertEquals(1, record.getGradeValue());
    }

    @Test
    void testConstructorWithCreditNotPassed() {
        Semester s1 = new Semester(1);
        SubjectRecord record = new SubjectRecord("PE", s1, ControlType.CREDIT, CreditStatus.NOT_PASSED);
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
        SubjectRecord r1 = new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT);
        SubjectRecord r2 = new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT);
        SubjectRecord rDifferentName = new SubjectRecord("Java", s1, ControlType.EXAM, Mark.EXCELLENT);
        SubjectRecord rDifferentSem = new SubjectRecord("Math", new Semester(2), ControlType.EXAM, Mark.EXCELLENT);
        SubjectRecord rDifferentType = new SubjectRecord("Math", s1, ControlType.CREDIT, CreditStatus.PASSED);

        // 1. Рефлексивность (this == o)
        assertEquals(r1, r1);

        // 2. Симметричность
        assertEquals(r1, r2);
        assertEquals(r2, r1);
        assertEquals(r1.hashCode(), r2.hashCode());

        // 3. Неравенство (null и другой класс)
        assertNotEquals(r1, null);
        assertNotEquals(r1, "Some String");

        // 4. Неравенство по полям
        assertNotEquals(r1, rDifferentName);
        assertNotEquals(r1, rDifferentSem);
        assertNotEquals(r1, rDifferentType);
    }

    @Test
    void testToString() {
        Semester s1 = new Semester(1);
        SubjectRecord rMark = new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT);
        SubjectRecord rCredit = new SubjectRecord("PE", s1, ControlType.CREDIT, CreditStatus.PASSED);

        // Исправлена ошибка Ambiguous method call: явно приводим null к типу Mark
        SubjectRecord rEmpty = new SubjectRecord("Lost", s1, ControlType.EXAM, (Mark) null);

        assertNotNull(rMark.toString());
        assertNotNull(rCredit.toString());
        assertNotNull(rEmpty.toString()); // Покрывает ветку "Нет оценки" или N/A
    }
}
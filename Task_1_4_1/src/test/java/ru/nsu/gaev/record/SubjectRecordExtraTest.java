package ru.nsu.gaev.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;

/**
 * Дополнительные тесты для SubjectRecord.
 */
public class SubjectRecordExtraTest {

    @Test
    public void testToStringNoGrade() {
        Semester s1 = new Semester(1);
        SubjectRecord r = new SubjectRecord(
                "Предмет", s1, ControlType.PRACTICE, (Mark) null);
        assertTrue(r.toString().contains("Нет оценки"));

        SubjectRecord credit = new SubjectRecord(
                "Предмет2", s1, ControlType.CREDIT, CreditStatus.NOT_PASSED);
        assertTrue(credit.toString().contains("Не зачет")
                || credit.toString().contains("Зачет"));
    }

    @Test
    public void testEqualsNullAndOtherType() {
        Semester s1 = new Semester(1);
        SubjectRecord r1 = new SubjectRecord(
                "Математика", s1, ControlType.EXAM, Mark.EXCELLENT_PLUS);
        assertNotEquals(null, r1);
        assertNotEquals("string", r1);
    }

    @Test
    public void testEqualsDifferentFields() {
        Semester s1 = new Semester(1);
        Semester s2 = new Semester(2);
        SubjectRecord a = new SubjectRecord(
                "A", s1, ControlType.EXAM, Mark.EXCELLENT_PLUS);
        SubjectRecord b = new SubjectRecord(
                "B", s1, ControlType.EXAM, Mark.EXCELLENT_PLUS);
        SubjectRecord c = new SubjectRecord(
                "A", s2, ControlType.EXAM, Mark.EXCELLENT_PLUS);
        SubjectRecord d = new SubjectRecord(
                "A", s1, ControlType.CREDIT, CreditStatus.PASSED);

        assertNotEquals(a, b);
        assertNotEquals(a, c);
        assertNotEquals(a, d);
    }

    @Test
    public void testGetGradeValueCreditAndMarkVariants() {
        Semester s1 = new Semester(1);
        SubjectRecord creditPassed = new SubjectRecord(
                "История", s1, ControlType.CREDIT, CreditStatus.PASSED);
        assertEquals(1, creditPassed.getGradeValue());

        SubjectRecord creditNotPassed = new SubjectRecord(
                "История", s1, ControlType.CREDIT, CreditStatus.NOT_PASSED);
        assertEquals(0, creditNotPassed.getGradeValue());

        SubjectRecord mark2 = new SubjectRecord(
                "Алгебра", s1, ControlType.EXAM, Mark.SATISFACTORY);
        assertEquals(2, mark2.getGradeValue());
    }
}

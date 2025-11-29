package ru.nsu.gaev.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;

/**
 * Тесты для класса SubjectRecord (запись об оценке).
 */
public class SubjectRecordTest {

    private Semester semester1;

    @BeforeEach
    public void setUp() {
        semester1 = new Semester(1);
    }

    @Test
    public void testSubjectRecordWithMark() {
        SubjectRecord record = new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS);

        assertEquals("Математика", record.getSubjectName());
        assertEquals(semester1, record.getSemester());
        assertEquals(ControlType.EXAM, record.getControlType());
        assertEquals(Mark.EXCELLENT_PLUS, record.getMark());
        assertNull(record.getCreditStatus());
        assertEquals(5, record.getGradeValue());
    }

    @Test
    public void testSubjectRecordWithCreditStatus() {
        SubjectRecord record = new SubjectRecord(
                "История", semester1, ControlType.CREDIT,
                CreditStatus.PASSED);

        assertEquals("История", record.getSubjectName());
        assertEquals(semester1, record.getSemester());
        assertEquals(ControlType.CREDIT, record.getControlType());
        assertNull(record.getMark());
        assertEquals(CreditStatus.PASSED, record.getCreditStatus());
        assertEquals(1, record.getGradeValue());
    }

    @Test
    public void testSubjectRecordCreditStatusNotPassed() {
        SubjectRecord record = new SubjectRecord(
                "История", semester1, ControlType.CREDIT,
                CreditStatus.NOT_PASSED);

        assertEquals(0, record.getGradeValue());
    }

    @Test
    public void testSubjectRecordCreditStatusWrongType() {
        assertThrows(IllegalArgumentException.class, () ->
                new SubjectRecord(
                        "Математика", semester1, ControlType.EXAM,
                        CreditStatus.PASSED)
        );
    }

    @Test
    public void testSubjectRecordEquality() {
        SubjectRecord record1 = new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS);
        SubjectRecord record1Copy = new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.GOOD);

        assertEquals(record1, record1Copy);
        assertEquals(record1.hashCode(), record1Copy.hashCode());
    }

    @Test
    public void testSubjectRecordToString() {
        SubjectRecord record = new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS);

        String expected = "Семестр 1 | Математика (Экзамен): Отлично";
        assertEquals(expected, record.toString());
    }

    @Test
    public void testSubjectRecordDifferentMarks() {
        assertEquals(2,
                new SubjectRecord(
                        "Предмет", semester1, ControlType.EXAM,
                        Mark.SATISFACTORY).getGradeValue());
        assertEquals(3,
                new SubjectRecord(
                        "Предмет", semester1, ControlType.EXAM,
                        Mark.GOOD).getGradeValue());
        assertEquals(4,
                new SubjectRecord(
                        "Предмет", semester1, ControlType.EXAM,
                        Mark.EXCELLENT).getGradeValue());
        assertEquals(5,
                new SubjectRecord(
                        "Предмет", semester1, ControlType.EXAM,
                        Mark.EXCELLENT_PLUS).getGradeValue());
    }
}

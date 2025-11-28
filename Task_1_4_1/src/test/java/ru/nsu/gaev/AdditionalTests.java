package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdditionalTests {

    @Test
    @DisplayName("DifferentiatedGrade: equals/hashCode/toString")
    void testDifferentiatedGradeEqualsHash() {
        DifferentiatedGrade g1 = new DifferentiatedGrade(5);
        DifferentiatedGrade g2 = new DifferentiatedGrade(5);
        DifferentiatedGrade g3 = new DifferentiatedGrade(4);

        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
        assertNotEquals(g1, g3);
        assertTrue(g1.toString().contains("5"));
    }

    @Test
    @DisplayName("CreditGrade: equals/hashCode/toString and values")
    void testCreditGradeEqualsHashAndToString() {
        CreditGrade c1 = new CreditGrade(true);
        CreditGrade c2 = new CreditGrade(true);
        CreditGrade c3 = new CreditGrade(false);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
        assertEquals(1, c1.getValue());
        assertEquals(0, c3.getValue());
        assertTrue(c1.toString().contains("Зачет"));
    }

    @Test
    @DisplayName("SubjectRecord: invalid differentiated grade throws")
    void testSubjectRecordInvalidDifferentiatedGrade() {
        assertThrows(IllegalArgumentException.class,
                () -> new SubjectRecord("X", 1, ControlType.EXAM, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new SubjectRecord("X", 1, ControlType.EXAM, 6));
    }

    @Test
    @DisplayName("SubjectRecord: credit grade returns CreditGrade instance")
    void testSubjectRecordCreditGradeInstance() {
        SubjectRecord sr = new SubjectRecord("PE", 1, ControlType.CREDIT, 1);
        assertTrue(sr.getGrade() instanceof CreditGrade);
        assertEquals(1, sr.getGradeValue());
    }

    @Test
    @DisplayName("ElectronicGradeBook: getRecords returns defensive copy")
    void testGetRecordsImmutable() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));

        List<SubjectRecord> out = book.getRecords();
        assertEquals(1, out.size());

        out.clear();
        assertEquals(1, book.getRecords().size());
    }

    @Test
    @DisplayName("Curriculum: getRequiredSubjects returns defensive copy")
    void testCurriculumListImmutabilityAndToString() {
        Curriculum cur = new Curriculum("CS Test");
        CurriculumSubject s1 = new CurriculumSubject("Math",
                new Semester(1), ControlType.EXAM);
        cur.addRequiredSubject(s1);

        List<CurriculumSubject> list = cur.getRequiredSubjects();
        assertEquals(1, list.size());

        list.clear();
        assertEquals(1, cur.getRequiredSubjects().size());

        assertTrue(cur.toString().contains("CS Test"));
    }

    @Test
    @DisplayName("CurriculumSubject: equals/hashCode/toString")
    void testCurriculumSubjectEqualsHashCodeToString() {
        CurriculumSubject a = new CurriculumSubject("Phys",
                new Semester(2), ControlType.EXAM);
        CurriculumSubject b = new CurriculumSubject("Phys",
                new Semester(2), ControlType.EXAM);
        CurriculumSubject c = new CurriculumSubject("Phys",
                new Semester(3), ControlType.EXAM);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertTrue(a.toString().contains("Phys"));
        assertTrue(a.toString().contains("Семестр"));
    }

    @Test
    @DisplayName("Student: toString and setCurriculum effect")
    void testStudentToStringAndSetCurriculum() {
        Student student = new Student("John Doe", "ID123", false);
        String s = student.toString();
        assertTrue(s.contains("John Doe"));
        assertTrue(s.contains("ID123"));
        assertTrue(s.contains("бюджетная") || s.contains("платная")
                || s.contains("Форма"));

        Curriculum cur = new Curriculum("Eng Program");
        student.setCurriculum(cur);
        assertTrue(student.toString().contains("Eng Program"));
    }
}

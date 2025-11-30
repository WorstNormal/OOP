package ru.nsu.gaev.curriculum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.grade.Mark;
import ru.nsu.gaev.record.SubjectRecord;

class CurriculumTest {

    @Test
    void testAreAllSubjectsCompleted() {
        Semester s1 = new Semester(1);
        Curriculum curriculum = new Curriculum("Test Plan");
        CurriculumSubject subj1 = new CurriculumSubject("Math", s1, ControlType.EXAM);
        CurriculumSubject subj2 = new CurriculumSubject("Java", s1, ControlType.EXAM);

        curriculum.addRequiredSubject(subj1);
        curriculum.addRequiredSubject(subj2);

        List<SubjectRecord> completed = new ArrayList<>();
        // Только 1 предмет сдан
        completed.add(new SubjectRecord("Math", s1, ControlType.EXAM, Mark.GOOD));
        assertFalse(curriculum.areAllSubjectsCompleted(completed));

        // Все предметы сданы
        completed.add(new SubjectRecord("Java", s1, ControlType.EXAM, Mark.EXCELLENT));
        assertTrue(curriculum.areAllSubjectsCompleted(completed));

        // Лишний предмет не ломает логику
        completed.add(new SubjectRecord("Extra", s1, ControlType.EXAM, Mark.GOOD));
        assertTrue(curriculum.areAllSubjectsCompleted(completed));
    }

    @Test
    void testGettersAndToString() {
        Curriculum c = new Curriculum("IT");
        assertEquals("IT", c.getCurriculumName());
        assertNotNull(c.getRequiredSubjects());
        assertTrue(c.getRequiredSubjects().isEmpty());
    }

    // --- Тесты для CurriculumSubject (повышают покрытие equals/hashCode) ---

    @Test
    void testCurriculumSubjectEquals() {
        Semester s1 = new Semester(1);
        CurriculumSubject cs1 = new CurriculumSubject("Math", s1, ControlType.EXAM);
        CurriculumSubject cs2 = new CurriculumSubject("Math", s1, ControlType.EXAM);
        CurriculumSubject csDiffName = new CurriculumSubject("Java", s1, ControlType.EXAM);
        CurriculumSubject csDiffSem = new CurriculumSubject("Math", new Semester(2), ControlType.EXAM);
        CurriculumSubject csDiffType = new CurriculumSubject("Math", s1, ControlType.CREDIT);

        assertEquals(cs1, cs1); // this == o
        assertEquals(cs1, cs2); // full match
        assertEquals(cs1.hashCode(), cs2.hashCode());

        assertNotEquals(cs1, null);
        assertNotEquals(cs1, new Object());
        assertNotEquals(cs1, csDiffName);
        assertNotEquals(cs1, csDiffSem);
        assertNotEquals(cs1, csDiffType);
    }

    @Test
    void testCurriculumSubjectGettersAndToString() {
        Semester s1 = new Semester(1);
        CurriculumSubject cs = new CurriculumSubject("Math", s1, ControlType.EXAM);
        assertEquals("Math", cs.getSubjectName());
        assertEquals(s1, cs.getSemester());
        assertEquals(ControlType.EXAM, cs.getControlType());
        assertNotNull(cs.toString());
    }
}
package ru.nsu.gaev.curriculum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.grade.Mark;
import ru.nsu.gaev.record.SubjectRecord;

/**
 * Тесты для класса Curriculum (учебный план).
 */
public class CurriculumTest {

    private Curriculum curriculum;
    private Semester semester1;
    private Semester semester2;

    /**
     * Подготовка фикстур перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        curriculum = new Curriculum("ОПП ФИТИП");
        semester1 = new Semester(1);
        semester2 = new Semester(2);

        curriculum.addRequiredSubject(
                new CurriculumSubject("Математика", semester1,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("Программирование", semester1,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("БД", semester2, ControlType.EXAM));
    }

    @Test
    public void testCurriculumCreation() {
        assertEquals("ОПП ФИТИП", curriculum.getCurriculumName());
        assertEquals(3, curriculum.getRequiredSubjects().size());
    }

    @Test
    public void testAllSubjectsCompleted() {
        List<SubjectRecord> completed = new ArrayList<>();
        completed.add(new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        completed.add(new SubjectRecord(
                "Программирование", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        completed.add(new SubjectRecord(
                "БД", semester2, ControlType.EXAM, Mark.EXCELLENT_PLUS));

        assertTrue(curriculum.areAllSubjectsCompleted(completed));
    }

    @Test
    public void testNotAllSubjectsCompleted() {
        List<SubjectRecord> completed = new ArrayList<>();
        completed.add(new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        completed.add(new SubjectRecord(
                "Программирование", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        

        assertFalse(curriculum.areAllSubjectsCompleted(completed));
    }

    @Test
    public void testGetMissingSubjects() {
        List<SubjectRecord> completed = new ArrayList<>();
        completed.add(new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        completed.add(new SubjectRecord(
                "Программирование", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        List<CurriculumSubject> missing =
                curriculum.getMissingSubjects(completed);
        assertEquals(1, missing.size());
        assertEquals("БД", missing.get(0).getSubjectName());
    }

    @Test
    public void testCurriculumToString() {
        String expected = "Учебный план: ОПП ФИТИП (3 предметов)";
        assertEquals(expected, curriculum.toString());
    }
}

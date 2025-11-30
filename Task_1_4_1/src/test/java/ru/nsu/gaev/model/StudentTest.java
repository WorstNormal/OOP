package ru.nsu.gaev.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.Mark;
import ru.nsu.gaev.record.SubjectRecord;

/**
 * Тесты для класса Student (студент).
 */
public class StudentTest {

    private Student student;
    private ElectronicGradeBook gradeBook;

    /**
     * Подготовка фикстур перед каждым тестом: создаём семестр и зачетную книжку,
     * добавляем одну запись и создаём объект студента.
     */
    @BeforeEach
    public void setUp() {
        Semester semester1 = new Semester(1);
        gradeBook = new ElectronicGradeBook(false, semester1);
        gradeBook.addRecord(new SubjectRecord(
                "Математика", semester1, ControlType.EXAM,
                Mark.EXCELLENT));

        student = new Student("Иван Иванович Иванов", "232302", gradeBook);
    }

    @Test
    public void testStudentCreation() {
        assertNotNull(student);
        assertEquals("Иван Иванович Иванов", student.getFullName());
        assertEquals("232302", student.getStudentId());
        assertNotNull(student.getGradeBook());
    }

    @Test
    public void testStudentGradeBook() {
        assertEquals(gradeBook, student.getGradeBook());
        assertEquals(1, student.getGradeBook().getRecords().size());
    }

    @Test
    public void testStudentEquality() {
        Semester semester1 = new Semester(1);
        ElectronicGradeBook otherGradeBook =
                new ElectronicGradeBook(false, semester1);
        Student student2 = new Student(
                "Петр Петрович Петров", "232302", otherGradeBook);

        assertEquals(student, student2);
    }

    @Test
    public void testStudentToString() {
        String expected =
                "Студент: Иван Иванович Иванов (номер зачетной книжки: "
                        + "232302)";
        assertEquals(expected, student.toString());
    }
}

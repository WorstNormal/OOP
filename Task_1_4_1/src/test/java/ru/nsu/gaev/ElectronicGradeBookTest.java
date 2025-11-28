package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ElectronicGradeBookTest {

    @Test
    @DisplayName("SubjectRecord: проверка геттеров и toString с новым Grade API")
    void testSubjectRecordMethods() {
        SubjectRecord record = new SubjectRecord("OOP", 2, ControlType.EXAM,
                new DifferentiatedGrade(5));

        assertEquals("OOP", record.getSubjectName());
        assertEquals(2, record.getSemester());
        assertEquals(ControlType.EXAM, record.getControlType());
        assertEquals(5, record.getGradeValue());

        assertNotNull(record.toString());
        assertTrue(record.toString().contains("OOP"));
    }

    @Test
    @DisplayName("SubjectRecord: совместимость со старым int API")
    void testSubjectRecordLegacyInt() {
        SubjectRecord examRecord = new SubjectRecord("Math", 1,
                ControlType.EXAM, 4);
        assertEquals(4, examRecord.getGradeValue());

        SubjectRecord creditRecord = new SubjectRecord("PE", 1,
                ControlType.CREDIT, 1);
        assertEquals(1, creditRecord.getGradeValue());
    }

    @Test
    @DisplayName("ControlType: проверка displayName и values")
    void testControlType() {
        assertEquals("Экзамен", ControlType.EXAM.getDisplayName());

        assertNotNull(ControlType.values());
        assertEquals(ControlType.EXAM, ControlType.valueOf("EXAM"));
    }

    @Test
    @DisplayName("DifferentiatedGrade: проверка значений")
    void testDifferentiatedGrade() {
        DifferentiatedGrade grade5 = new DifferentiatedGrade(5);
        assertEquals(5, grade5.getValue());
        assertEquals("5", grade5.getDisplayName());

        DifferentiatedGrade grade2 = new DifferentiatedGrade(2);
        assertEquals(2, grade2.getValue());

        assertThrows(IllegalArgumentException.class,
                () -> new DifferentiatedGrade(1));
        assertThrows(IllegalArgumentException.class,
                () -> new DifferentiatedGrade(6));
    }

    @Test
    @DisplayName("CreditGrade: проверка зачета и незачета")
    void testCreditGrade() {
        CreditGrade passed = new CreditGrade(true);
        assertEquals(1, passed.getValue());
        assertEquals("Зачет", passed.getDisplayName());
        assertTrue(passed.isPassed());

        CreditGrade notPassed = new CreditGrade(false);
        assertEquals(0, notPassed.getValue());
        assertEquals("Не зачет", notPassed.getDisplayName());
        assertFalse(notPassed.isPassed());
    }

    @Test
    @DisplayName("Средний балл: пустая зачетка")
    void testAverageGrade_Empty() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        assertEquals(0.0, book.calculateAverageGrade());
    }

    @Test
    @DisplayName("Средний балл: игнорирование зачетов и расчет")
    void testAverageGrade_Calculation() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        book.addRecord(new SubjectRecord("PE", 1, ControlType.CREDIT,
                new CreditGrade(true)));
        book.addRecord(new SubjectRecord("History", 1,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(5)));

        assertEquals(4.5, book.calculateAverageGrade());
    }

    @Test
    @DisplayName("Бюджет: уже на бюджете (early exit)")
    void testBudget_AlreadyBudget() {
        ElectronicGradeBook book = new ElectronicGradeBook(false);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM,
                new DifferentiatedGrade(2)));
        assertTrue(book.canTransferToBudget(2));
    }

    @Test
    @DisplayName("Бюджет: первый семестр (недостаточно данных)")
    void testBudget_FirstSemester() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        assertFalse(book.canTransferToBudget(1));
    }

    @Test
    @DisplayName("Бюджет: нет записей за последние сессии")
    void testBudget_NoRecords() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        assertFalse(book.canTransferToBudget(3));
    }

    @Test
    @DisplayName("Бюджет: тройка за экзамен (fail)")
    void testBudget_FailExamThree() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM,
                new DifferentiatedGrade(3)));
        assertFalse(book.canTransferToBudget(2));
    }

    @Test
    @DisplayName("Бюджет: двойка (fail)")
    void testBudget_FailGradeTwo() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("History", 2,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(2)));
        assertFalse(book.canTransferToBudget(2));
    }

    @Test
    @DisplayName("Бюджет: успех (тройка за диф.зачет допустима)")
    void testBudget_Success() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        book.addRecord(new SubjectRecord("Physics", 2,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(3)));
        assertTrue(book.canTransferToBudget(2));
    }

    @Test
    @DisplayName("Диплом: нет оценок")
    void testDiploma_Empty() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        assertFalse(book.canGetRedDiploma());
    }

    @Test
    @DisplayName("Диплом: есть тройка (fail)")
    void testDiploma_FailOnThree() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("History", 2, ControlType.EXAM,
                new DifferentiatedGrade(3)));
        assertFalse(book.canGetRedDiploma());
    }

    @Test
    @DisplayName("Диплом: ВКР не на отлич (fail)")
    void testDiploma_FailThesis() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("WKR", 8, ControlType.THESIS,
                new DifferentiatedGrade(4)));
        assertFalse(book.canGetRedDiploma());
    }

    @Test
    @DisplayName("Диплом: меньше 75% пятерок (fail)")
    void testDiploma_FailPercentage() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("Phys", 2, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        book.addRecord(new SubjectRecord("WKR", 8, ControlType.THESIS,
                new DifferentiatedGrade(5)));
        assertFalse(book.canGetRedDiploma());
    }

    @Test
    @DisplayName("Диплом: успех")
    void testDiploma_Success() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("S1", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("S2", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("S3", 3, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("S4", 4, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        book.addRecord(new SubjectRecord("WKR", 8, ControlType.THESIS,
                new DifferentiatedGrade(5)));

        assertTrue(book.canGetRedDiploma());
    }

    @Test
    @DisplayName("Диплом: проверка учебного плана")
    void testDiploma_WithCurriculum() {
        Curriculum curriculum = new Curriculum("Test Plan");
        curriculum.addRequiredSubject(
                new CurriculumSubject("Math", new Semester(1),
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("Physics", new Semester(2),
                        ControlType.EXAM));

        ElectronicGradeBook book = new ElectronicGradeBook(true, curriculum);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("WKR", 8, ControlType.THESIS,
                new DifferentiatedGrade(5)));

        assertFalse(book.canGetRedDiploma());
    }

    @Test
    @DisplayName("Стипендия: нет предметов в текущем семестре")
    void testScholarship_NoCurrentSubjects() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        assertFalse(book.canGetIncreasedScholarship(2));
    }

    @Test
    @DisplayName("Стипендия: есть четверка (fail)")
    void testScholarship_FailGrade() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        assertFalse(book.canGetIncreasedScholarship(2));
    }

    @Test
    @DisplayName("Стипендия: незачет (fail)")
    void testScholarship_FailCredit() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("PE", 2, ControlType.CREDIT,
                new CreditGrade(false)));
        assertFalse(book.canGetIncreasedScholarship(2));
    }

    @Test
    @DisplayName("Стипендия: успех")
    void testScholarship_Success() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("PE", 2, ControlType.CREDIT,
                new CreditGrade(true)));
        assertTrue(book.canGetIncreasedScholarship(2));
    }

    @Test
    @DisplayName("Semester: создание и геттер")
    void testSemester() {
        Semester sem1 = new Semester(1);
        assertEquals(1, sem1.getSemesterNumber());

        assertThrows(IllegalArgumentException.class,
                () -> new Semester(0));
        assertThrows(IllegalArgumentException.class,
                () -> new Semester(-1));
    }

    @Test
    @DisplayName("Student: создание и геттеры")
    void testStudent() {
        Curriculum curriculum = new Curriculum("Test");
        Student student = new Student("John Doe", "123456", true,
                curriculum);

        assertEquals("John Doe", student.getFullName());
        assertEquals("123456", student.getStudentId());
        assertTrue(student.isPaidEducation());
        assertEquals(curriculum, student.getCurriculum());
        assertNotNull(student.getGradeBook());
    }

    @Test
    @DisplayName("Student: добавление оценок")
    void testStudent_AddRecords() {
        Student student = new Student("Jane Smith", "654321", false);
        student.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));

        assertEquals(1, student.getGradeBook().getRecords().size());
    }

    @Test
    @DisplayName("Curriculum: добавление предметов и проверка завершения")
    void testCurriculum() {
        Curriculum curriculum = new Curriculum("CS Program");
        CurriculumSubject subject1 = new CurriculumSubject("Math",
                new Semester(1), ControlType.EXAM);
        CurriculumSubject subject2 = new CurriculumSubject("Physics",
                new Semester(2), ControlType.EXAM);

        curriculum.addRequiredSubject(subject1);
        curriculum.addRequiredSubject(subject2);

        assertEquals(2, curriculum.getRequiredSubjects().size());

        java.util.List<SubjectRecord> records = new java.util.ArrayList<>();
        records.add(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        records.add(new SubjectRecord("Physics", 2, ControlType.EXAM,
                new DifferentiatedGrade(4)));

        assertTrue(curriculum.areAllSubjectsCompleted(records));
        assertEquals(0, curriculum.getMissingSubjects(records).size());
    }

    @Test
    @DisplayName("Curriculum: проверка недостающих предметов")
    void testCurriculum_MissingSubjects() {
        Curriculum curriculum = new Curriculum("CS Program");
        curriculum.addRequiredSubject(new CurriculumSubject("Math",
                new Semester(1), ControlType.EXAM));
        curriculum.addRequiredSubject(new CurriculumSubject("Physics",
                new Semester(2), ControlType.EXAM));

        java.util.List<SubjectRecord> records = new java.util.ArrayList<>();
        records.add(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));

        assertFalse(curriculum.areAllSubjectsCompleted(records));
        assertEquals(1, curriculum.getMissingSubjects(records).size());
    }

    @Test
    @DisplayName("Main: запуск")
    void testMainClass() {
        Main.main(new String[]{});
    }
}

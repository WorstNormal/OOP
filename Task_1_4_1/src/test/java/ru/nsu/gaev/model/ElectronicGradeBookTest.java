package ru.nsu.gaev.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Curriculum;
import ru.nsu.gaev.curriculum.CurriculumSubject;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;
import ru.nsu.gaev.record.SubjectRecord;

class ElectronicGradeBookTest {

    @Test
    void testCalculateAverage() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, new Semester(1));
        assertEquals(0.0, book.calculateAverageGrade());

        book.addRecord(new SubjectRecord("Math", new Semester(1), ControlType.EXAM, Mark.EXCELLENT)); // 5
        book.addRecord(new SubjectRecord("Java", new Semester(1), ControlType.EXAM, Mark.SATISFACTORY)); // 3
        // Зачеты игнорируются
        book.addRecord(new SubjectRecord("PE", new Semester(1), ControlType.CREDIT, CreditStatus.PASSED));

        assertEquals(4.0, book.calculateAverageGrade(), 0.001);
    }

    @Test
    void testBudgetTransfer() {
        Semester s1 = new Semester(1);
        Semester s2 = new Semester(2);

        // 1. Платник без истории -> False (null prev)
        ElectronicGradeBook book = new ElectronicGradeBook(true, s1);
        assertFalse(book.canTransferToBudget());

        // 2. Бюджетник -> True
        ElectronicGradeBook freeBook = new ElectronicGradeBook(false, s2);
        assertTrue(freeBook.canTransferToBudget());

        // 3. Платник с тройкой за экзамен -> False
        book = new ElectronicGradeBook(true, s2);
        book.addRecord(new SubjectRecord("Math", s1, ControlType.EXAM, Mark.SATISFACTORY));
        assertFalse(book.canTransferToBudget());

        // 4. Платник с тройкой за ДИФФ ЗАЧЕТ -> True (по условию задачи)
        book = new ElectronicGradeBook(true, s2);
        book.addRecord(new SubjectRecord("Project", s1, ControlType.DIFF_CREDIT, Mark.SATISFACTORY));
        assertTrue(book.canTransferToBudget());
    }

    @Test
    void testRedDiploma() {
        Semester s1 = new Semester(1);
        ElectronicGradeBook book = new ElectronicGradeBook(true, s1);

        // Нет оценок -> False
        assertFalse(book.canGetRedDiploma());

        // Есть 5, но нет ВКР -> False
        book.addRecord(new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT));
        assertFalse(book.canGetRedDiploma());

        // Есть 5 и ВКР на 5 -> True
        book.addRecord(new SubjectRecord("Thesis", s1, ControlType.THESIS, Mark.EXCELLENT));
        assertTrue(book.canGetRedDiploma());

        // Добавили тройку -> False
        book.addRecord(new SubjectRecord("FailSubject", s1, ControlType.EXAM, Mark.SATISFACTORY));
        assertFalse(book.canGetRedDiploma());
    }

    @Test
    void testRedDiplomaWithCurriculum() {
        Semester s1 = new Semester(1);
        Curriculum c = new Curriculum("Test");
        c.addRequiredSubject(new CurriculumSubject("Math", s1, ControlType.EXAM));

        ElectronicGradeBook book = new ElectronicGradeBook(true, s1, c);
        // Предмет не сдан
        assertFalse(book.canGetRedDiploma());

        book.addRecord(new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT));
        // Предмет сдан, но нет ВКР (требуется логикой метода)
        // Добавим ВКР
        book.addRecord(new SubjectRecord("Thesis", s1, ControlType.THESIS, Mark.EXCELLENT));

        assertTrue(book.canGetRedDiploma());
    }

    @Test
    void testScholarship() {
        Semester s1 = new Semester(1);
        Semester s2 = new Semester(2);
        ElectronicGradeBook book = new ElectronicGradeBook(true, s2);

        // Нет истории -> False
        assertFalse(book.canGetIncreasedScholarship());

        // Прошлый семестр пустой -> False
        // (Мы имитируем это, просто не добавляя записи за s1)

        // Добавим хорошую историю
        book.addRecord(new SubjectRecord("Math", s1, ControlType.EXAM, Mark.EXCELLENT));
        assertTrue(book.canGetIncreasedScholarship());

        // Добавим 4 -> False
        book.addRecord(new SubjectRecord("Hist", s1, ControlType.EXAM, Mark.GOOD));
        assertFalse(book.canGetIncreasedScholarship());
    }
}
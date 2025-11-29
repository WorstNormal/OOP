package ru.nsu.gaev.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Curriculum;
import ru.nsu.gaev.curriculum.CurriculumSubject;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;
import ru.nsu.gaev.record.SubjectRecord;

/**
 * Тесты для класса ElectronicGradeBook (электронная зачетная книжка).
 * Проверяет функционал расчета среднего балла, возможности перевода на
 * бюджет, получения красного диплома и повышенной стипендии.
 */
public class ElectronicGradeBookTest {

    private ElectronicGradeBook gradeBook;
    private Semester semester1;
    private Semester semester2;
    private Semester semester3;
    private Semester semester8;

    /**
     * Подготовка фикстур перед каждым тестом.
     */
    @BeforeEach
    public void setUp() {
        semester1 = new Semester(1);
        semester2 = new Semester(2);
        semester3 = new Semester(3);
        semester8 = new Semester(8);

        gradeBook = new ElectronicGradeBook(true, semester3);
    }

    @Test
    public void testAverageGradeEmpty() {
        assertEquals(0.0, gradeBook.calculateAverageGrade());
    }

    @Test
    public void testAverageGradeWithMarks() {
        gradeBook.addRecord(new SubjectRecord(
                "Предмет1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Предмет2", semester1, ControlType.EXAM,
                Mark.GOOD));
        gradeBook.addRecord(new SubjectRecord(
                "Предмет3", semester2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        double expected = (5.0 + 3.0 + 5.0) / 3;
        assertEquals(expected,
                gradeBook.calculateAverageGrade(), 0.0001);
    }

    @Test
    public void testAverageGradeIgnoresCredits() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Зачет", semester1, ControlType.CREDIT,
                CreditStatus.PASSED));

        assertEquals(5.0, gradeBook.calculateAverageGrade());
    }

    @Test
    public void testTransferToBudgetNotPaidEducation() {
        ElectronicGradeBook budgetBook =
                new ElectronicGradeBook(false, semester2);
        assertTrue(budgetBook.canTransferToBudget());
    }

    @Test
    public void testTransferToBudgetFirstSemester() {
        ElectronicGradeBook paidBook = new ElectronicGradeBook(true, semester1);
        assertFalse(paidBook.canTransferToBudget());
    }

    @Test
    public void testTransferToBudgetNoThreesInLastTwoSemesters() {
        ElectronicGradeBook paidBook = new ElectronicGradeBook(true, semester3);

        paidBook.addRecord(new SubjectRecord(
                "Экзамен1", semester2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        paidBook.addRecord(new SubjectRecord(
                "Экзамен2", semester2, ControlType.EXAM,
                Mark.GOOD));
        paidBook.addRecord(new SubjectRecord(
                "Экзамен3", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        assertTrue(paidBook.canTransferToBudget());
    }

    @Test
    public void testTransferToBudgetWithThreeInLastSemester() {
        ElectronicGradeBook paidBook = new ElectronicGradeBook(true, semester3);

        paidBook.addRecord(new SubjectRecord(
                "Экзамен1", semester2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        paidBook.addRecord(new SubjectRecord(
                "Экзамен2", semester3, ControlType.EXAM,
                Mark.SATISFACTORY));

        assertFalse(paidBook.canTransferToBudget());
    }

    @Test
    public void testRedDiplomaNoThesis() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    public void testRedDiplomaThesisNotExcellent() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.GOOD));

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    public void testRedDiplomaWithThree() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester1, ControlType.EXAM,
                Mark.SATISFACTORY));
        gradeBook.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    public void testRedDiplomaSuccess() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен3", semester2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен4", semester2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertTrue(gradeBook.canGetRedDiploma());
    }

    @Test
    public void testRedDiploma75PercentExcellent() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен3", semester2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен4", semester2, ControlType.EXAM,
                Mark.GOOD));
        gradeBook.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertTrue(gradeBook.canGetRedDiploma());
    }

    @Test
    public void testRedDiplomaLessThan75PercentExcellent() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен3", semester2, ControlType.EXAM,
                Mark.GOOD));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен4", semester2, ControlType.EXAM,
                Mark.GOOD));
        gradeBook.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertFalse(gradeBook.canGetRedDiploma());
    }

    @Test
    public void testRedDiplomaWithCurriculum() {
        Curriculum curriculum = new Curriculum("ОПП");
        curriculum.addRequiredSubject(
                new CurriculumSubject("Экзамен1", semester1,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("ВКР", semester8, ControlType.THESIS));

        ElectronicGradeBook bookWithCurriculum =
                new ElectronicGradeBook(false, semester8, curriculum);

        bookWithCurriculum.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        bookWithCurriculum.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertTrue(bookWithCurriculum.canGetRedDiploma());
    }

    @Test
    public void testRedDiplomaWithCurriculumMissing() {
        Curriculum curriculum = new Curriculum("ОПП");
        curriculum.addRequiredSubject(
                new CurriculumSubject("Экзамен1", semester1,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("Экзамен2", semester2,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("ВКР", semester8, ControlType.THESIS));

        ElectronicGradeBook bookWithCurriculum =
                new ElectronicGradeBook(false, semester8, curriculum);

        bookWithCurriculum.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        bookWithCurriculum.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertFalse(bookWithCurriculum.canGetRedDiploma());
    }

    @Test
    public void testIncreasedScholarshipEmpty() {
        assertFalse(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    public void testIncreasedScholarshipAllExcellent() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Зачет", semester3, ControlType.CREDIT,
                CreditStatus.PASSED));

        assertTrue(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    public void testIncreasedScholarshipWithNonExcellent() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester3, ControlType.EXAM,
                Mark.GOOD));

        assertFalse(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    public void testIncreasedScholarshipWithFailedCredit() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Зачет", semester3, ControlType.CREDIT,
                CreditStatus.NOT_PASSED));

        assertFalse(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    public void testIncreasedScholarshipOnlyInCurrentSemester() {
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        gradeBook.addRecord(new SubjectRecord(
                "Экзамен2", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        assertTrue(gradeBook.canGetIncreasedScholarship());
    }

    @Test
    public void testCurrentSemesterManagement() {
        assertEquals(semester3, gradeBook.getCurrentSemester());
        assertTrue(gradeBook.isPaidEducation());
    }

    @Test
    public void testTransferToBudgetAllowsSatisfactoryForDiffCredit() {
        ElectronicGradeBook paidBook = new ElectronicGradeBook(true, semester3);

        paidBook.addRecord(new SubjectRecord(
                "ПредметДифф", semester2, ControlType.DIFF_CREDIT,
                Mark.SATISFACTORY));
        paidBook.addRecord(new SubjectRecord(
                "ЭкзаменТекущий", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        assertTrue(paidBook.canTransferToBudget(),
                "DIFF_CREDIT с удовлетворительно не должен блокировать перевод");
    }

    @Test
    public void testDynamicCurriculumChangeAffectsRedDiploma() {
        ElectronicGradeBook book = new ElectronicGradeBook(false, semester8);

        book.addRecord(new SubjectRecord(
                "Экзамен1", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        book.addRecord(new SubjectRecord(
                "Экзамен2", semester2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        book.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertTrue(book.canGetRedDiploma());

        Curriculum curriculum = new Curriculum("ТестПлан");
        curriculum.addRequiredSubject(new CurriculumSubject(
                "Экзамен1", semester1, ControlType.EXAM));
        curriculum.addRequiredSubject(new CurriculumSubject(
                "Экзамен2", semester2, ControlType.EXAM));
        curriculum.addRequiredSubject(new CurriculumSubject(
                "Экзамен3", semester3, ControlType.EXAM));
        curriculum.addRequiredSubject(new CurriculumSubject(
                "ВКР", semester8, ControlType.THESIS));

        book.setCurriculum(curriculum);

        assertFalse(book.canGetRedDiploma());

        book.addRecord(new SubjectRecord(
                "Экзамен3", semester3, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        assertTrue(book.canGetRedDiploma());
    }

    @Test
    public void testGetAndSetCurriculumAccessor() {
        ElectronicGradeBook book = new ElectronicGradeBook(false, semester3);
        assertEquals(null, book.getCurriculum());

        Curriculum curriculum = new Curriculum("План");
        book.setCurriculum(curriculum);
        assertEquals(curriculum, book.getCurriculum());
    }

    @Test
    public void testCanTransferToBudgetNoRecordsInLastTwoSessions() {
        ElectronicGradeBook paidBook = new ElectronicGradeBook(true, semester3);
        paidBook.addRecord(new SubjectRecord(
                "Давний", semester1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));

        assertFalse(paidBook.canTransferToBudget());
    }

    @Test
    public void testRedDiplomaOnlyThesisDoesNotPass() {
        ElectronicGradeBook book = new ElectronicGradeBook(false, semester8);
        book.addRecord(new SubjectRecord(
                "ВКР", semester8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        assertFalse(book.canGetRedDiploma());
    }
}

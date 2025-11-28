package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ElectronicGradeBookExtraTests {

    @Test
    @DisplayName("calculateAverageGrade: normal and empty cases")
    void testCalculateAverageGrade() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("Physics", 1,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(4)));
        book.addRecord(new SubjectRecord("PE", 1, ControlType.CREDIT,
                new CreditGrade(true)));
        double avg = book.calculateAverageGrade();
        assertEquals(4.5, avg, 1e-9);

        ElectronicGradeBook onlyCredits = new ElectronicGradeBook(true);
        onlyCredits.addRecord(new SubjectRecord("PE", 1,
                ControlType.CREDIT, new CreditGrade(true)));
        assertEquals(0.0, onlyCredits.calculateAverageGrade(), 1e-9);
    }

    @Test
    @DisplayName("canTransferToBudget: paid/unpaid and failing cases")
    void testCanTransferToBudget() {
        // unpaid should always return true
        ElectronicGradeBook unpaid = new ElectronicGradeBook(false);
        assertTrue(unpaid.canTransferToBudget(1));

        ElectronicGradeBook paid = new ElectronicGradeBook(true);
        // currentSemester = 1 -> previous < 1 -> false
        assertFalse(paid.canTransferToBudget(1));

        // prepare semesters 1 and 2 with passing grades
        paid.addRecord(new SubjectRecord("A", 1, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        paid.addRecord(new SubjectRecord("B", 2, ControlType.DIFF_CREDIT,
                new DifferentiatedGrade(3)));
        assertTrue(paid.canTransferToBudget(2));

        // exam == 3 should disqualify
        ElectronicGradeBook paid2 = new ElectronicGradeBook(true);
        paid2.addRecord(new SubjectRecord("X", 1, ControlType.EXAM,
                new DifferentiatedGrade(3)));
        paid2.addRecord(new SubjectRecord("Y", 2, ControlType.DIFF_CREDIT,
                new DifferentiatedGrade(4)));
        assertFalse(paid2.canTransferToBudget(2));

        // grade < 3 should disqualify
        ElectronicGradeBook paid3 = new ElectronicGradeBook(true);
        paid3.addRecord(new SubjectRecord("X", 1, ControlType.EXAM,
                new DifferentiatedGrade(2)));
        paid3.addRecord(new SubjectRecord("Y", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        assertFalse(paid3.canTransferToBudget(2));
    }

    @Test
    @DisplayName("canGetRedDiploma: success and failure cases")
    void testCanGetRedDiploma() {
        Curriculum curriculum = new Curriculum("TestCur");
        curriculum.addRequiredSubject(new CurriculumSubject("Math",
                new Semester(1), ControlType.EXAM));
        curriculum.addRequiredSubject(new CurriculumSubject("Phys",
                new Semester(1), ControlType.DIFF_CREDIT));

        ElectronicGradeBook book = new ElectronicGradeBook(true, curriculum);
        // missing subjects -> false
        assertFalse(book.canGetRedDiploma());

        // add required completed subjects
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("Phys", 1, ControlType.DIFF_CREDIT,
                new DifferentiatedGrade(5)));
        // add other non-credit records including thesis
        book.addRecord(new SubjectRecord("Other", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("Thesis", 8, ControlType.THESIS,
                new DifferentiatedGrade(5)));

        assertTrue(book.canGetRedDiploma());

        // if any grade == 3 -> false
        ElectronicGradeBook book2 = new ElectronicGradeBook(true, curriculum);
        book2.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book2.addRecord(new SubjectRecord("Phys", 1,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(3)));
        book2.addRecord(new SubjectRecord("Thesis", 8, ControlType.THESIS,
                new DifferentiatedGrade(5)));
        assertFalse(book2.canGetRedDiploma());

        // thesis not excellent -> false
        ElectronicGradeBook book3 = new ElectronicGradeBook(true, curriculum);
        book3.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book3.addRecord(new SubjectRecord("Phys", 1, ControlType.DIFF_CREDIT,
                new DifferentiatedGrade(5)));
        book3.addRecord(new SubjectRecord("Thesis", 8, ControlType.THESIS,
                new DifferentiatedGrade(4)));
        assertFalse(book3.canGetRedDiploma());

        // less than 75% excellent -> false
        ElectronicGradeBook book4 = new ElectronicGradeBook(true, curriculum);
        book4.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book4.addRecord(new SubjectRecord("Phys", 1, ControlType.DIFF_CREDIT,
                new DifferentiatedGrade(4)));
        book4.addRecord(new SubjectRecord("Other", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book4.addRecord(new SubjectRecord("Thesis", 8, ControlType.THESIS,
                new DifferentiatedGrade(5)));
        // Math(5), Phys(4), Other(5), Thesis(5) -> excellent=3/4 = 0.75
        assertTrue(book4.canGetRedDiploma());

        // now make it 2/4 -> 0.5
        ElectronicGradeBook book5 = new ElectronicGradeBook(true, curriculum);
        book5.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        book5.addRecord(new SubjectRecord("Phys", 1, ControlType.DIFF_CREDIT,
                new DifferentiatedGrade(4)));
        book5.addRecord(new SubjectRecord("Other", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book5.addRecord(new SubjectRecord("Thesis", 8, ControlType.THESIS,
                new DifferentiatedGrade(5)));
        assertFalse(book5.canGetRedDiploma());
    }

    @Test
    @DisplayName("canGetIncreasedScholarship: success and failure cases")
    void testCanGetIncreasedScholarship() {
        ElectronicGradeBook book = new ElectronicGradeBook(true);
        // empty current session -> false
        assertFalse(book.canGetIncreasedScholarship(1));

        // success: all non-credit grades 5 and credits passed
        book.addRecord(new SubjectRecord("A", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book.addRecord(new SubjectRecord("B", 2, ControlType.CREDIT,
                new CreditGrade(true)));
        assertTrue(book.canGetIncreasedScholarship(2));

        // fail if any non-credit < 5
        ElectronicGradeBook book2 = new ElectronicGradeBook(true);
        book2.addRecord(new SubjectRecord("A", 2, ControlType.EXAM,
                new DifferentiatedGrade(4)));
        book2.addRecord(new SubjectRecord("B", 2, ControlType.CREDIT,
                new CreditGrade(true)));
        assertFalse(book2.canGetIncreasedScholarship(2));

        // fail if credit not passed
        ElectronicGradeBook book3 = new ElectronicGradeBook(true);
        book3.addRecord(new SubjectRecord("A", 2, ControlType.EXAM,
                new DifferentiatedGrade(5)));
        book3.addRecord(new SubjectRecord("B", 2, ControlType.CREDIT,
                new CreditGrade(false)));
        assertFalse(book3.canGetIncreasedScholarship(2));
    }
}

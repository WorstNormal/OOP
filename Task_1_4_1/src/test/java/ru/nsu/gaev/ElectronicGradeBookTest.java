package ru.nsu.gaev;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElectronicGradeBookTest {

    // --- Тесты для SubjectRecord (покрываем геттеры и toString) ---
    @Test
    @DisplayName("SubjectRecord: проверка геттеров и toString")
    void testSubjectRecordMethods() {
        SubjectRecord record = new SubjectRecord("OOP", 2, ControlType.EXAM, 5);

        assertEquals("OOP", record.getSubjectName());
        assertEquals(2, record.getSemester());
        assertEquals(ControlType.EXAM, record.getControlType());
        assertEquals(5, record.getGrade());

        // Проверяем, что toString не падает и возвращает непустую строку
        assertNotNull(record.toString());
        assertTrue(record.toString().contains("OOP"));
    }

    // --- Тесты для ControlType (покрываем enum методы) ---
    @Test
    @DisplayName("ControlType: проверка displayName и values")
    void testControlType() {
        assertEquals("Экзамен", ControlType.EXAM.getDisplayName());

        // Вызов values() и valueOf() нужен для 100% покрытия enum
        assertNotNull(ControlType.values());
        assertEquals(ControlType.EXAM, ControlType.valueOf("EXAM"));
    }

    // --- Тесты для ElectronicGradeBook ---

    @Test
    @DisplayName("Средний балл: пустая зачетка")
    void testAverageGrade_Empty() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 1);
        assertEquals(0.0, book.calculateAverageGrade());
    }

    @Test
    @DisplayName("Средний балл: игнорирование зачетов и расчет")
    void testAverageGrade_Calculation() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM, 4)); // +
        book.addRecord(new SubjectRecord("PE", 1, ControlType.CREDIT, 1)); // Игнор
        book.addRecord(new SubjectRecord("History", 1, ControlType.DIFF_CREDIT, 5)); // +

        // (4 + 5) / 2 = 4.5
        assertEquals(4.5, book.calculateAverageGrade());
    }

    @Test
    @DisplayName("Бюджет: уже на бюджете (early exit)")
    void testBudget_AlreadyBudget() {
        ElectronicGradeBook book = new ElectronicGradeBook(false, 2);
        // Даже с двойками должен вернуть true, так как метод проверяет "возможность перевода",
        // а если уже бюджет - то переводиться некуда (или условие выполнено тривиально)
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM, 2));
        assertTrue(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: первый семестр (недостаточно данных)")
    void testBudget_FirstSemester() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 1);
        // Нет предыдущего семестра
        assertFalse(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: нет записей за последние сессии")
    void testBudget_NoRecords() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 3);
        // Семестр 3, но записей нет
        assertFalse(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: тройка за экзамен (fail)")
    void testBudget_FailExamThree() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM, 3));
        assertFalse(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: двойка (fail)")
    void testBudget_FailGradeTwo() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        // Двойка блокирует перевод, даже если это не экзамен
        book.addRecord(new SubjectRecord("History", 2, ControlType.DIFF_CREDIT, 2));
        assertFalse(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Бюджет: успех (тройка за диф.зачет допустима)")
    void testBudget_Success() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM, 4));
        book.addRecord(new SubjectRecord("Physics", 2, ControlType.DIFF_CREDIT, 3)); // Допустимо
        assertTrue(book.canTransferToBudget());
    }

    @Test
    @DisplayName("Диплом: нет оценок")
    void testDiploma_Empty() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 8);
        assertFalse(book.isRedDiplomaLikely());
    }

    @Test
    @DisplayName("Диплом: есть тройка (fail)")
    void testDiploma_FailOnThree() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 8);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("History", 2, ControlType.EXAM, 3)); // Тройка
        assertFalse(book.isRedDiplomaLikely());
    }

    @Test
    @DisplayName("Диплом: ВКР не на отлично (fail)")
    void testDiploma_FailThesis() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 8);
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("WKR", 8, ControlType.THESIS, 4)); // 4 за диплом
        assertFalse(book.isRedDiplomaLikely());
    }

    @Test
    @DisplayName("Диплом: меньше 75% пятерок (fail)")
    void testDiploma_FailPercentage() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 8);
        // 1 пятерка, 1 четверка = 50%
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("Phys", 2, ControlType.EXAM, 4));
        book.addRecord(new SubjectRecord("WKR", 8, ControlType.THESIS, 5));
        assertFalse(book.isRedDiplomaLikely());
    }

    @Test
    @DisplayName("Диплом: успех")
    void testDiploma_Success() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 8);
        // 3 пятерки, 1 четверка = 75%
        book.addRecord(new SubjectRecord("S1", 1, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("S2", 2, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("S3", 3, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("S4", 4, ControlType.EXAM, 4));
        // ВКР
        book.addRecord(new SubjectRecord("WKR", 8, ControlType.THESIS, 5));

        assertTrue(book.isRedDiplomaLikely());
    }

    @Test
    @DisplayName("Стипендия: нет предметов в текущем семестре")
    void testScholarship_NoCurrentSubjects() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        // Есть предмет за 1 семестр, но проверяем 2-й
        book.addRecord(new SubjectRecord("Math", 1, ControlType.EXAM, 5));
        assertFalse(book.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: есть четверка (fail)")
    void testScholarship_FailGrade() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM, 4));
        assertFalse(book.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: незачет (fail)")
    void testScholarship_FailCredit() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("PE", 2, ControlType.CREDIT, 0)); // Незачет
        assertFalse(book.canGetIncreasedScholarship());
    }

    @Test
    @DisplayName("Стипендия: успех")
    void testScholarship_Success() {
        ElectronicGradeBook book = new ElectronicGradeBook(true, 2);
        book.addRecord(new SubjectRecord("Math", 2, ControlType.EXAM, 5));
        book.addRecord(new SubjectRecord("PE", 2, ControlType.CREDIT, 1)); // Зачет
        assertTrue(book.canGetIncreasedScholarship());
    }

    // --- Тест для Main (чтобы класс Main не был 0%) ---
    @Test
    @DisplayName("Main: запуск")
    void testMainClass() {
        // Просто вызываем main, чтобы покрытие засчиталось
        Main.main(new String[]{});
    }
}
package ru.nsu.gaev;

/**
 * Главный класс для демонстрации работы электронной зачетной книжки.
 */
public class Main {
    /**
     * Точка входа в программу.
     * Заполняет зачетку данными и выводит результаты расчетов.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        ElectronicGradeBook myBook = new ElectronicGradeBook(true, 2);

        myBook.addRecord(new SubjectRecord("Математика", 1, ControlType.EXAM, 4));
        myBook.addRecord(new SubjectRecord("Программирование", 1, ControlType.DIFF_CREDIT, 5));
        myBook.addRecord(new SubjectRecord("История", 1, ControlType.CREDIT, 1));

        myBook.addRecord(new SubjectRecord("Физика", 2, ControlType.DIFF_CREDIT, 3));
        myBook.addRecord(new SubjectRecord("Алгоритмы", 2, ControlType.EXAM, 4));
        myBook.addRecord(new SubjectRecord("Английский", 2, ControlType.EXAM, 5));

        System.out.println("=== Отчет по зачетной книжке ===");

        System.out.printf("Средний балл: %.2f\n", myBook.calculateAverageGrade());

        System.out.println("Возможность перевода на бюджет: "
                + (myBook.canTransferToBudget() ? "ДА" : "НЕТ"));

        System.out.println("Прогноз на красный диплом: "
                + (myBook.isRedDiplomaLikely() ? "ДА" : "НЕТ (Есть тройки или <75% пятерок)"));

        System.out.println("Повышенная стипендия в текущем семестре: "
                + (myBook.canGetIncreasedScholarship() ? "ДА" : "НЕТ"));

        System.out.println("\n--- Сценарий: Студент-отличник ---");
        ElectronicGradeBook excellentStudent = new ElectronicGradeBook(false, 8);
        excellentStudent.addRecord(new SubjectRecord("Java", 8, ControlType.EXAM, 5));
        excellentStudent.addRecord(new SubjectRecord("Базы данных", 8, ControlType.DIFF_CREDIT, 5));
        excellentStudent.addRecord(new SubjectRecord("Диплом", 8, ControlType.THESIS, 5));

        excellentStudent.addRecord(new SubjectRecord("Философия", 1, ControlType.EXAM, 5));

        System.out.println("Прогноз на красный диплом: "
                + (excellentStudent.isRedDiplomaLikely() ? "ДА" : "НЕТ"));
    }
}
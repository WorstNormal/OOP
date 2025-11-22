package ru.nsu.gaev;

public class Main {
    public static void main(String[] args) {
        // Создаем зачетку для студента на платном, сейчас 2-й семестр
        ElectronicGradeBook myBook = new ElectronicGradeBook(true, 2);

        // --- 1 Семестр ---
        myBook.addRecord(new SubjectRecord("Математика", 1, ControlType.EXAM, 4));
        myBook.addRecord(new SubjectRecord("Программирование", 1, ControlType.DIFF_CREDIT, 5));
        myBook.addRecord(new SubjectRecord("История", 1, ControlType.CREDIT, 1)); // Сдал

        // --- 2 Семестр ---
        // Допустим, студент получил тройку за Диф.Зачет (по условию это не блокирует перевод на бюджет)
        myBook.addRecord(new SubjectRecord("Физика", 2, ControlType.DIFF_CREDIT, 3));
        // Но экзамены сдал хорошо
        myBook.addRecord(new SubjectRecord("Алгоритмы", 2, ControlType.EXAM, 4));
        myBook.addRecord(new SubjectRecord("Английский", 2, ControlType.EXAM, 5));

        System.out.println("=== Отчет по зачетной книжке ===");

        // 1. Средний балл
        // (4 + 5 + 3 + 4 + 5) / 5 = 21 / 5 = 4.2
        System.out.printf("Средний балл: %.2f\n", myBook.calculateAverageGrade());

        // 2. Перевод на бюджет
        // Условие: нет троек за экзамены за последние 2 сессии.
        // У нас тройка за Диф.Зачет (Физика), но экзамены 4 и 5. Должно быть true.
        System.out.println("Возможность перевода на бюджет: " +
                (myBook.canTransferToBudget() ? "ДА" : "НЕТ"));

        // 3. Красный диплом
        // Сейчас есть тройка (Физика). Для красного диплома тройки недопустимы вообще.
        System.out.println("Прогноз на красный диплом: " +
                (myBook.isRedDiplomaLikely() ? "ДА" : "НЕТ (Есть тройки или <75% пятерок)"));

        // 4. Повышенная стипендия
        // Текущий семестр (2) имеет оценки 3, 4, 5. Для повышенной нужны все 5.
        System.out.println("Повышенная стипендия в текущем семестре: " +
                (myBook.canGetIncreasedScholarship() ? "ДА" : "НЕТ"));


        System.out.println("\n--- Сценарий: Студент-отличник ---");
        ElectronicGradeBook excellentStudent = new ElectronicGradeBook(false, 8);
        excellentStudent.addRecord(new SubjectRecord("Java", 8, ControlType.EXAM, 5));
        excellentStudent.addRecord(new SubjectRecord("Базы данных", 8, ControlType.DIFF_CREDIT, 5));
        excellentStudent.addRecord(new SubjectRecord("Диплом", 8, ControlType.THESIS, 5));

        // Добавим старую оценку, чтобы проверить %
        excellentStudent.addRecord(new SubjectRecord("Философия", 1, ControlType.EXAM, 5));

        System.out.println("Прогноз на красный диплом: " +
                (excellentStudent.isRedDiplomaLikely() ? "ДА" : "НЕТ"));
    }
}
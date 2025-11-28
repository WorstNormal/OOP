package ru.nsu.gaev;

/**
 * Главный класс для демонстрации работы электронной зачетной книжки.
 */
public class Main {
    /**
     * Точка входа в программу.
     * Создает студентов с учебным планом и выводит результаты расчетов.
     *
     * @param args Аргументы командной строки (не используются).
     */
    public static void main(String[] args) {
        Curriculum computerScienceCurriculum = new Curriculum(
                "Информатика и вычислительная техника");
        computerScienceCurriculum.addRequiredSubject(
                new CurriculumSubject("Математика", new Semester(1),
                        ControlType.EXAM));
        computerScienceCurriculum.addRequiredSubject(
                new CurriculumSubject("Программирование", new Semester(1),
                        ControlType.DIFF_CREDIT));
        computerScienceCurriculum.addRequiredSubject(
                new CurriculumSubject("История", new Semester(1),
                        ControlType.CREDIT));
        computerScienceCurriculum.addRequiredSubject(
                new CurriculumSubject("Физика", new Semester(2),
                        ControlType.DIFF_CREDIT));
        computerScienceCurriculum.addRequiredSubject(
                new CurriculumSubject("Алгоритмы", new Semester(2),
                        ControlType.EXAM));
        computerScienceCurriculum.addRequiredSubject(
                new CurriculumSubject("Английский", new Semester(2),
                        ControlType.EXAM));

        Student student1 = new Student("Иван Петров", "БИ-210001", true,
                computerScienceCurriculum);
        student1.addRecord(new SubjectRecord("Математика", 1,
                ControlType.EXAM, new DifferentiatedGrade(4)));
        student1.addRecord(new SubjectRecord("Программирование", 1,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(5)));
        student1.addRecord(new SubjectRecord("История", 1,
                ControlType.CREDIT, new CreditGrade(true)));
        student1.addRecord(new SubjectRecord("Физика", 2,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(3)));
        student1.addRecord(new SubjectRecord("Алгоритмы", 2,
                ControlType.EXAM, new DifferentiatedGrade(4)));
        student1.addRecord(new SubjectRecord("Английский", 2,
                ControlType.EXAM, new DifferentiatedGrade(5)));

        System.out.println("=== Студент 1: Платное обучение ===");
        System.out.println(student1);
        System.out.printf("Средний балл: %.2f\n",
                student1.getGradeBook().calculateAverageGrade());
        System.out.println("Возможность перевода на бюджет (семестр 2): "
                + (student1.getGradeBook().canTransferToBudget(2)
                ? "ДА" : "НЕТ"));
        System.out.println("Повышенная стипендия в семестре 2: "
                + (student1.getGradeBook().canGetIncreasedScholarship(2)
                ? "ДА" : "НЕТ"));
        System.out.println("Прогноз на красный диплом: "
                + (student1.getGradeBook().canGetRedDiploma()
                ? "ДА" : "НЕТ (Есть тройки)"));

        System.out.println("\n=== Студент 2: Отличник ===");

        Curriculum honorCurriculum = new Curriculum("Почетный учебный план");
        honorCurriculum.addRequiredSubject(
                new CurriculumSubject("Java", new Semester(8),
                        ControlType.EXAM));
        honorCurriculum.addRequiredSubject(
                new CurriculumSubject("Базы данных", new Semester(8),
                        ControlType.DIFF_CREDIT));
        honorCurriculum.addRequiredSubject(
                new CurriculumSubject("Диплом", new Semester(8),
                        ControlType.THESIS));
        honorCurriculum.addRequiredSubject(
                new CurriculumSubject("Философия", new Semester(1),
                        ControlType.EXAM));

        Student excellentStudent = new Student("Алексей Иванов",
                "БИ-200001", false, honorCurriculum);
        excellentStudent.addRecord(new SubjectRecord("Java", 8,
                ControlType.EXAM, new DifferentiatedGrade(5)));
        excellentStudent.addRecord(new SubjectRecord("Базы данных", 8,
                ControlType.DIFF_CREDIT, new DifferentiatedGrade(5)));
        excellentStudent.addRecord(new SubjectRecord("Диплом", 8,
                ControlType.THESIS, new DifferentiatedGrade(5)));
        excellentStudent.addRecord(new SubjectRecord("Философия", 1,
                ControlType.EXAM, new DifferentiatedGrade(5)));

        System.out.println(excellentStudent);
        System.out.printf("Средний балл: %.2f\n",
                excellentStudent.getGradeBook().calculateAverageGrade());
        System.out.println("Повышенная стипендия в семестре 8: "
                + (excellentStudent.getGradeBook()
                .canGetIncreasedScholarship(8) ? "ДА" : "НЕТ"));
        System.out.println("Прогноз на красный диплом: "
                + (excellentStudent.getGradeBook().canGetRedDiploma()
                ? "ДА" : "НЕТ"));

        System.out.println("\n=== Демонстрация учебного плана ===");
        if (excellentStudent.getCurriculum() != null) {
            System.out.println("Все предметы пройдены: "
                    + excellentStudent.getCurriculum()
                    .areAllSubjectsCompleted(
                            excellentStudent.getGradeBook().getRecords()));
            System.out.println("Недостающих предметов: "
                    + excellentStudent.getCurriculum().getMissingSubjects(
                            excellentStudent.getGradeBook().getRecords())
                    .size());
        }

        System.out.println("\n=== Совместимость со старым API ===");
        Student legacyStudent = new Student("Петр Сидоров", "БИ-210002",
                true);
        legacyStudent.addRecord(new SubjectRecord("Математика", 1,
                ControlType.EXAM, 4));
        legacyStudent.addRecord(new SubjectRecord("История", 1,
                ControlType.CREDIT, 1));

        System.out.println(legacyStudent);
        System.out.printf("Средний балл: %.2f\n",
                legacyStudent.getGradeBook().calculateAverageGrade());
    }
}
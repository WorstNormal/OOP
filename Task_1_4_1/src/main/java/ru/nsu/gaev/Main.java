package ru.nsu.gaev;

import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Curriculum;
import ru.nsu.gaev.curriculum.CurriculumSubject;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;
import ru.nsu.gaev.model.ElectronicGradeBook;
import ru.nsu.gaev.model.Student;
import ru.nsu.gaev.record.SubjectRecord;

/**
 * Короткая демонстрация использования модели электронной зачетной книжки.
 */
public class Main {
    /**
     * Точка входа приложения-примера.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Semester s1 = new Semester(1);
        Semester s2 = new Semester(2);
        Semester s8 = new Semester(8);

        Curriculum curriculum = new Curriculum("ОПП ФИТ");
        curriculum.addRequiredSubject(
                new CurriculumSubject("Математический анализ", s1,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("Программирование", s1,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("Дискретная математика", s2,
                        ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("Защита ВКР", s8,
                        ControlType.THESIS));

        ElectronicGradeBook book = new ElectronicGradeBook(true, s8, curriculum);

        
        book.addRecord(new SubjectRecord(
                "Математический анализ", s1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        book.addRecord(new SubjectRecord(
                "Программирование", s1, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        book.addRecord(new SubjectRecord(
                "Физкультура", s1, ControlType.CREDIT,
                CreditStatus.PASSED));
        book.addRecord(new SubjectRecord(
                "Дискретная математика", s2, ControlType.EXAM,
                Mark.EXCELLENT_PLUS));
        
        book.addRecord(new SubjectRecord(
                "Защита ВКР", s8, ControlType.THESIS,
                Mark.EXCELLENT_PLUS));

        Student student = new Student("Иванов И.И.", "123456", book);

        System.out.println(student);
        System.out.println("Средний балл: " + book.calculateAverageGrade());
        System.out.println("Может перевестись на бюджет: " + book.canTransferToBudget());
        System.out.println("Может получить красный диплом: " + book.canGetRedDiploma());
        System.out.println("Может получить повышенную стипендию (текущий семестр): "
                + book.canGetIncreasedScholarship());
    }
}

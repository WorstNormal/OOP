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
 * Демонстрация работы системы.
 */
public class Main {
    /**
     * Точка входа.
     *
     * @param args аргументы
     */
    public static void main(String[] args) {
        Semester s1 = Semester.of(1, 1);
        Semester s2 = Semester.of(1, 2);
        Semester s8 = Semester.of(4, 2);

        Curriculum curriculum = new Curriculum("ФИТ Бакалавриат");
        curriculum.addRequiredSubject(
                new CurriculumSubject("Матан", s1, ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("Прога", s1, ControlType.EXAM));
        curriculum.addRequiredSubject(
                new CurriculumSubject("ВКР", s8, ControlType.THESIS));

        ElectronicGradeBook book = new ElectronicGradeBook(true, s8, curriculum);

        book.addRecord(new SubjectRecord("Матан", s1, ControlType.EXAM, Mark.EXCELLENT));
        book.addRecord(new SubjectRecord("Прога", s1, ControlType.EXAM, Mark.EXCELLENT));
        book.addRecord(new SubjectRecord("Физра", s1, ControlType.CREDIT, CreditStatus.PASSED));
        book.addRecord(new SubjectRecord("ВКР", s8, ControlType.THESIS, Mark.EXCELLENT));

        Student student = new Student("Иванов И.И.", "123456", book);

        System.out.println("Студент: " + student);
        System.out.println("Средний балл: " + book.calculateAverageGrade());
        System.out.println("Красный диплом: " + book.canGetRedDiploma());
        System.out.println("Перевод на бюджет: " + book.canTransferToBudget());
    }
}
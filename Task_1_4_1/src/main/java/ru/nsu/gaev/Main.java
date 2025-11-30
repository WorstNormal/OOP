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
        Semester s8 = Semester.of(4, 2);

        Curriculum curriculum = new Curriculum("ФИТ Бакалавриат");
        curriculum.addRequiredSubject(new CurriculumSubject("Матан", s1, ControlType.EXAM));
        curriculum.addRequiredSubject(new CurriculumSubject("Прога", s1, ControlType.EXAM));
        curriculum.addRequiredSubject(new CurriculumSubject("ВКР", s8, ControlType.THESIS));

        ElectronicGradeBook initialBook = new ElectronicGradeBook(true, s8, curriculum);
        initialBook.addRecord(new SubjectRecord("Матан", s1, ControlType.EXAM, Mark.EXCELLENT));
        initialBook.addRecord(new SubjectRecord("Прога", s1, ControlType.EXAM, Mark.EXCELLENT));
        initialBook.addRecord(new SubjectRecord("Физра", s1, ControlType.CREDIT, CreditStatus.PASSED));
        initialBook.addRecord(new SubjectRecord("ВКР", s8, ControlType.THESIS, Mark.EXCELLENT));

        Student student = new Student("Иванов И.И.", "123456", initialBook);

        System.out.println("=== Демонстрация методов класса Student ===");

        System.out.println("1. ФИО студента: " + student.getFullName());

        System.out.println("2. Номер зачетной книжки: " + student.getStudentId());

        System.out.println("3. Строковое представление (toString): " + student);

        ElectronicGradeBook studentBook = student.getGradeBook();

        System.out.println("4. Данные из полученной зачетной книжки:");
        Semester current = studentBook.getCurrentSemester();
        System.out.println("   - Текущий курс: " + current.getCourseNumber());
        System.out.println("   - Средний балл: " + studentBook.calculateAverageGrade());
        System.out.println("   - Красный диплом: " + (studentBook.canGetRedDiploma() ? "Да" : "Нет"));

        Student sameIdStudent = new Student("Петров П.П.", "123456", null);
        boolean areEqual = student.equals(sameIdStudent);
        System.out.println("5. Сравнение через equals (по ID): " + areEqual);

        System.out.println("6. Хеш-коды:");
        System.out.println("   - HashCode оригинала: " + student.hashCode());
        System.out.println("   - HashCode копии:     " + sameIdStudent.hashCode());

        if (areEqual && student.hashCode() == sameIdStudent.hashCode()) {
            System.out.println("   -> Контракт equals/hashCode соблюден.");
        } else {
            System.out.println("   -> Ошибка контракта equals/hashCode!");
        }
    }
}
package ru.nsu.gaev.model;

/**
 * Класс, представляющий студента.
 * Содержит информацию о студенте и его электронную зачетную книжку.
 */
public final class Student {
    private final String fullName;
    private final String studentId;
    private final ElectronicGradeBook gradeBook;

    /**
     * Конструктор студента.
     *
     * @param fullName полное имя студента
     * @param studentId номер зачетной книжки
     * @param gradeBook электронная зачетная книжка
     */
    public Student(String fullName, String studentId,
                   ElectronicGradeBook gradeBook) {
        this.fullName = fullName;
        this.studentId = studentId;
        this.gradeBook = gradeBook;
    }

    public String getFullName() {
        return fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public ElectronicGradeBook getGradeBook() {
        return gradeBook;
    }

    @Override
    public String toString() {
        return "Студент: " + fullName + " (номер зачетной книжки: "
                + studentId + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode();
    }
}

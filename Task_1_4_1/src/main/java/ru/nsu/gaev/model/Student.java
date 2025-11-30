package ru.nsu.gaev.model;

import java.util.Objects;

/**
 * Класс студента.
 */
public final class Student {
    private final String fullName;
    private final String studentId;
    private final ElectronicGradeBook gradeBook;

    /**
     * Создает студента.
     *
     * @param fullName ФИО
     * @param studentId номер зачетки
     * @param gradeBook зачетная книжка
     */
    public Student(String fullName, String studentId, ElectronicGradeBook gradeBook) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return fullName + " (" + studentId + ")";
    }
}
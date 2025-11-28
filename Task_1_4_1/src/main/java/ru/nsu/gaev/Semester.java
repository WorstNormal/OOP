package ru.nsu.gaev;

/**
 * Класс, представляющий семестр обучения.
 * Содержит информацию о номере семестра.
 */
public class Semester {
    private final int semesterNumber;

    /**
     * Конструктор семестра.
     *
     * @param semesterNumber номер семестра (1, 2, 3 и т.д.)
     */
    public Semester(int semesterNumber) {
        if (semesterNumber < 1) {
            throw new IllegalArgumentException("Номер семестра должен быть >= 1");
        }
        this.semesterNumber = semesterNumber;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester semester = (Semester) o;
        return semesterNumber == semester.semesterNumber;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(semesterNumber);
    }

    @Override
    public String toString() {
        return "Семестр " + semesterNumber;
    }
}

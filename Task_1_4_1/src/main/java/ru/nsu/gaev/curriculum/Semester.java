package ru.nsu.gaev.curriculum;

import java.util.Objects;
import java.util.Optional;

/**
 * Представляет определенный семестр в учебном плане.
 *
 * <p>Этот класс является неизменяемым и предоставляет методы для навигации между семестрами
 * и получения контекстной информации, такой как номер курса.
 */
public final class Semester implements Comparable<Semester> {

    private final int semesterNumber;

    /**
     * Создает новый экземпляр класса Semester.
     *
     * @param semesterNumber номер семестра (должен быть больше или равен 1)
     * @throws IllegalArgumentException если номер семестра меньше 1
     */
    public Semester(int semesterNumber) {
        if (semesterNumber < 1) {
            throw new IllegalArgumentException("Номер семестра должен быть >= 1");
        }
        this.semesterNumber = semesterNumber;
    }

    /**
     * Создает семестр на основе номера курса и порядкового номера семестра в учебном году.
     *
     * @param course номер курса (1, 2, ...)
     * @param semesterInYear номер семестра в учебном году (1 или 2)
     * @return новый экземпляр класса Semester
     * @throws IllegalArgumentException если аргументы некорректны
     */
    public static Semester of(int course, int semesterInYear) {
        if (course < 1 || semesterInYear < 1 || semesterInYear > 2) {
            throw new IllegalArgumentException("Некорректный номер курса или семестра в году");
        }
        return new Semester((course - 1) * 2 + semesterInYear);
    }

    /**
     * Возвращает номер семестра.
     *
     * @return целое число, представляющее номер семестра
     */
    public int getSemesterNumber() {
        return semesterNumber;
    }

    /**
     * Вычисляет номер курса, соответствующий данному семестру.
     *
     * @return номер курса (например, 1 для 1-го и 2-го семестров)
     */
    public int getCourseNumber() {
        return (semesterNumber + 1) / 2;
    }

    /**
     * Проверяет, является ли семестр чётным.
     *
     * @return true, если номер семестра чётный
     */
    public boolean isEven() {
        return semesterNumber % 2 == 0;
    }

    /**
     * Проверяет, является ли семестр нечётным.
     *
     * @return true, если номер семестра нечётный
     */
    public boolean isOdd() {
        return !isEven();
    }

    /**
     * Создает новый экземпляр Semester, представляющий следующий семестр.
     *
     * @return следующий семестр
     */
    public Semester next() {
        return new Semester(semesterNumber + 1);
    }

    /**
     * Создает Optional, содержащий предыдущий семестр, если он существует.
     *
     * @return Optional с предыдущим семестром или пустой Optional, если текущий семестр первый
     */
    public Optional<Semester> previous() {
        if (semesterNumber <= 1) {
            return Optional.empty();
        }
        return Optional.of(new Semester(semesterNumber - 1));
    }

    @Override
    public int compareTo(Semester other) {
        return Integer.compare(this.semesterNumber, other.semesterNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Semester semester = (Semester) o;
        return semesterNumber == semester.semesterNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(semesterNumber);
    }

    @Override
    public String toString() {
        return "Семестр " + semesterNumber;
    }
}
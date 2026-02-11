package ru.nsu.gaev.curriculum;

import java.util.Objects;
import java.util.Optional;

/**
 * Представляет определенный семестр в учебном плане.
 *
 * <p>Класс неизменяем. Предоставляет навигацию (предыдущий/следующий)
 * и методы создания через статические фабрики.
 */
public final class Semester implements Comparable<Semester> {

    private final int semesterNumber;

    /**
     * Создает новый экземпляр класса Semester.
     *
     * @param semesterNumber номер семестра (должен быть >= 1)
     */
    public Semester(int semesterNumber) {
        if (semesterNumber < 1) {
            throw new IllegalArgumentException("Номер семестра должен быть >= 1");
        }
        this.semesterNumber = semesterNumber;
    }

    /**
     * Создает семестр по номеру курса и номеру семестра в году.
     *
     * @param course курс (1..)
     * @param semesterInYear семестр в году (1 или 2)
     * @return экземпляр Semester
     */
    public static Semester of(int course, int semesterInYear) {
        if (course < 1 || semesterInYear < 1 || semesterInYear > 2) {
            throw new IllegalArgumentException("Некорректные данные курса/семестра");
        }
        return new Semester((course - 1) * 2 + semesterInYear);
    }

    /**
     * Возвращает абсолютный номер семестра.
     *
     * @return номер семестра
     */
    public int getSemesterNumber() {
        return semesterNumber;
    }

    /**
     * Возвращает номер курса для текущего семестра.
     *
     * @return номер курса
     */
    public int getCourseNumber() {
        return (semesterNumber + 1) / 2;
    }

    /**
     * Проверяет, является ли семестр чётным (весенним).
     *
     * @return true, если номер семестра чётный
     */
    public boolean isEven() {
        return semesterNumber % 2 == 0;
    }

    /**
     * Проверяет, является ли семестр нечётным (осенним).
     *
     * @return true, если номер семестра нечётный
     */
    public boolean isOdd() {
        return !isEven();
    }

    /**
     * Возвращает следующий семестр.
     *
     * @return новый экземпляр Semester
     */
    public Semester next() {
        return new Semester(semesterNumber + 1);
    }

    /**
     * Возвращает предыдущий семестр, если он существует.
     *
     * @return Optional с предыдущим семестром или empty
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
package ru.nsu.gaev;

/**
 * Класс, представляющий дифференцированную оценку (2-5).
 */
public class DifferentiatedGrade implements Grade {
    private final int gradeValue;

    /**
     * Конструктор дифференцированной оценки.
     *
     * @param gradeValue значение оценки (2-5)
     * @throws IllegalArgumentException если оценка не в диапазоне 2-5
     */
    public DifferentiatedGrade(int gradeValue) {
        if (gradeValue < 2 || gradeValue > 5) {
            throw new IllegalArgumentException("Оценка должна быть в диапазоне 2-5");
        }
        this.gradeValue = gradeValue;
    }

    @Override
    public int getValue() {
        return gradeValue;
    }

    @Override
    public String getDisplayName() {
        return String.valueOf(gradeValue);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DifferentiatedGrade that = (DifferentiatedGrade) o;
        return gradeValue == that.gradeValue;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(gradeValue);
    }
}

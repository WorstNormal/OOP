package ru.nsu.gaev;

/**
 * Класс, представляющий простой зачет (зачет/не зачет).
 */
public class CreditGrade implements Grade {
    private final boolean passed;

    /**
     * Конструктор зачета.
     *
     * @param passed true - зачет, false - не зачет
     */
    public CreditGrade(boolean passed) {
        this.passed = passed;
    }

    public boolean isPassed() {
        return passed;
    }

    @Override
    public int getValue() {
        return passed ? 1 : 0;
    }

    @Override
    public String getDisplayName() {
        return passed ? "Зачет" : "Не зачет";
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CreditGrade that = (CreditGrade) o;
        return passed == that.passed;
    }

    @Override
    public int hashCode() {
        return Boolean.hashCode(passed);
    }
}

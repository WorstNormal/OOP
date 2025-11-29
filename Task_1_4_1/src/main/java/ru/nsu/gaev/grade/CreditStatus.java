package ru.nsu.gaev.grade;

/**
 * Перечисление для представления статуса зачета.
 */
public enum CreditStatus {
    PASSED("Зачет"),
    NOT_PASSED("Не зачет");

    private final String displayName;

    CreditStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isPassed() {
        return this == PASSED;
    }
}


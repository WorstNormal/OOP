package ru.nsu.gaev.grade;

/**
 * Статус зачета (сдан/не сдан).
 */
public enum CreditStatus {
    PASSED("Зачет"),
    NOT_PASSED("Не зачет");

    private final String displayName;

    CreditStatus(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает текстовое представление.
     *
     * @return название статуса
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Проверяет, сдан ли зачет.
     *
     * @return true если сдан
     */
    public boolean isPassed() {
        return this == PASSED;
    }
}
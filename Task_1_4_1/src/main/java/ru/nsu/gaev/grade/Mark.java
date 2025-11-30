package ru.nsu.gaev.grade;

/**
 * Перечисление для дифференцированных оценок.
 */
public enum Mark {
    BAD(2, "Неудовлетворительно"),
    SATISFACTORY(3, "Удовлетворительно"),
    GOOD(4, "Хорошо"),
    EXCELLENT(5, "Отлично");

    private final int value;
    private final String displayName;

    Mark(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    /**
     * Возвращает числовое значение оценки.
     *
     * @return значение (2-5)
     */
    public int getValue() {
        return value;
    }

    /**
     * Возвращает текстовое представление.
     *
     * @return название оценки
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Получает enum по числовому значению.
     *
     * @param value число (2-5)
     * @return соответствующий Mark
     */
    public static Mark fromValue(int value) {
        for (Mark mark : Mark.values()) {
            if (mark.value == value) {
                return mark;
            }
        }
        throw new IllegalArgumentException("Некорректное значение оценки: " + value);
    }
}
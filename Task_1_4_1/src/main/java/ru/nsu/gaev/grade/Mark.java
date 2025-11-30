package ru.nsu.gaev.grade;

/**
 * Перечисление для представления оценок (дифференцированных) в системе.
 * Использование enum гарантирует типобезопасность и исключает недопустимые значения.
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

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Преобразует числовое значение в перечисление Mark.
     *
     * @param value числовое значение (2-5)
     * @return соответствующее значение Mark
     * @throws IllegalArgumentException если значение вне диапазона 2-5
     */
    public static Mark fromValue(int value) {
        for (Mark mark : Mark.values()) {
            if (mark.value == value) {
                return mark;
            }
        }
        throw new IllegalArgumentException(
                "Оценка должна быть в диапазоне 2-5, получено: " + value
        );
    }
}


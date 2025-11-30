package ru.nsu.gaev.curriculum;

/**
 * Перечисление типов контроля успеваемости.
 */
public enum ControlType {
    EXAM("Экзамен"),
    DIFF_CREDIT("Дифференцированный зачет"),
    CREDIT("Зачет"),
    COURSEWORK("Курсовая работа"),
    PRACTICE("Практика"),
    THESIS("Защита ВКР");

    private final String displayName;

    ControlType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Возвращает читаемое название типа контроля.
     *
     * @return название
     */
    public String getDisplayName() {
        return displayName;
    }
}
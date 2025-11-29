package ru.nsu.gaev.curriculum;

/**
 * Перечисление типов контроля успеваемости.
 * Используется для разграничения логики перевода на бюджет и красного диплома.
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

    public String getDisplayName() {
        return displayName;
    }
}


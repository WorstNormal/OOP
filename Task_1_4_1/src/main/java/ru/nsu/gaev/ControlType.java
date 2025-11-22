package ru.nsu.gaev;

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
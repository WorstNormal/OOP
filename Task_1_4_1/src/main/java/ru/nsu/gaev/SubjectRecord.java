package ru.nsu.gaev;

/**
 * Класс, представляющий запись об оценке по предмету в зачетной книжке.
 * Избавляет от костылей при использовании интерфейса Grade для разных типов оценок.
 */
public class SubjectRecord {
    private final String subjectName;
    private final int semester;
    private final ControlType controlType;
    private final Grade grade;

    /**
     * Конструктор записи об оценке.
     *
     * @param subjectName название предмета
     * @param semester номер семестра
     * @param controlType тип контроля (экзамен, зачет и т.д.)
     * @param grade оценка (DifferentiatedGrade или CreditGrade)
     */
    public SubjectRecord(String subjectName, int semester,
                         ControlType controlType, Grade grade) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;
        this.grade = grade;
    }

    /**
     * Альтернативный конструктор для совместимости со старым кодом.
     * Автоматически преобразует int в соответствующую реализацию Grade.
     *
     * @param subjectName название предмета
     * @param semester номер семестра
     * @param controlType тип контроля
     * @param gradeValue числовое значение оценки
     */
    public SubjectRecord(String subjectName, int semester,
                         ControlType controlType, int gradeValue) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;

        if (controlType == ControlType.CREDIT) {
            this.grade = new CreditGrade(gradeValue == 1);
        } else {
            if (gradeValue < 2 || gradeValue > 5) {
                throw new IllegalArgumentException(
                        "Для дифференцированной оценки значение должно быть 2-5"
                );
            }
            this.grade = new DifferentiatedGrade(gradeValue);
        }
    }

    public int getSemester() {
        return semester;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public Grade getGrade() {
        return grade;
    }

    /**
     * Получить числовое значение оценки для совместимости.
     *
     * @return числовое значение оценки
     */
    public int getGradeValue() {
        return grade.getValue();
    }

    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public String toString() {
        return "Семестр " + semester + " | " + subjectName + " ("
                + controlType.getDisplayName() + "): "
                + grade.getDisplayName();
    }
}
package ru.nsu.gaev.curriculum;

/**
 * Класс, представляющий требуемый предмет в учебном плане.
 * Содержит информацию о названии предмета, семестре и типе контроля.
 */
public final class CurriculumSubject {
    private final String subjectName;
    private final Semester semester;
    private final ControlType controlType;

    /**
     * Конструктор требуемого предмета.
     *
     * @param subjectName название предмета
     * @param semester семестр, в котором должен быть пройден предмет
     * @param controlType тип контроля (экзамен, зачет и т.д.)
     */
    public CurriculumSubject(String subjectName, Semester semester,
                             ControlType controlType) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Semester getSemester() {
        return semester;
    }

    public ControlType getControlType() {
        return controlType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CurriculumSubject that = (CurriculumSubject) o;
        return subjectName.equals(that.subjectName)
                && semester.equals(that.semester)
                && controlType == that.controlType;
    }

    @Override
    public int hashCode() {
        return subjectName.hashCode() * 31 + semester.hashCode() * 31
                + controlType.hashCode();
    }

    @Override
    public String toString() {
        return subjectName + " (" + controlType.getDisplayName() + ") - "
                + semester;
    }
}


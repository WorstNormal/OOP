package ru.nsu.gaev.curriculum;

import java.util.Objects;

/**
 * Представляет требование учебного плана по конкретному предмету.
 *
 * <p>Определяет, какой предмет, в каком семестре и с каким типом контроля должен быть сдан.
 */
public final class CurriculumSubject {

    private final String subjectName;
    private final Semester semester;
    private final ControlType controlType;

    /**
     * Создает требование к предмету.
     *
     * @param subjectName название предмета
     * @param semester семестр сдачи
     * @param controlType тип контроля
     */
    public CurriculumSubject(String subjectName, Semester semester, ControlType controlType) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;
    }

    /**
     * Возвращает название предмета.
     *
     * @return название
     */
    public String getSubjectName() {
        return subjectName;
    }

    /**
     * Возвращает семестр сдачи.
     *
     * @return семестр
     */
    public Semester getSemester() {
        return semester;
    }

    /**
     * Возвращает тип контроля.
     *
     * @return тип контроля
     */
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
        return Objects.equals(subjectName, that.subjectName)
                && Objects.equals(semester, that.semester)
                && controlType == that.controlType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectName, semester, controlType);
    }

    @Override
    public String toString() {
        return subjectName + " (" + controlType.getDisplayName() + ") - " + semester;
    }
}
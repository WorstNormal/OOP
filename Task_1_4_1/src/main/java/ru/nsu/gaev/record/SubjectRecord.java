package ru.nsu.gaev.record;

import java.util.Objects;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;

/**
 * Запись в зачетной книжке о сдаче предмета.
 */
public final class SubjectRecord {

    private final String subjectName;
    private final Semester semester;
    private final ControlType controlType;
    private final Mark mark;
    private final CreditStatus creditStatus;

    /**
     * Создает запись с оценкой.
     *
     * @param subjectName предмет
     * @param semester семестр
     * @param controlType тип контроля
     * @param mark оценка
     */
    public SubjectRecord(String subjectName, Semester semester,
                         ControlType controlType, Mark mark) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;
        this.mark = mark;
        this.creditStatus = null;
    }

    /**
     * Создает запись с зачетом.
     *
     * @param subjectName предмет
     * @param semester семестр
     * @param controlType тип контроля (CREDIT)
     * @param creditStatus статус
     */
    public SubjectRecord(String subjectName, Semester semester,
                         ControlType controlType, CreditStatus creditStatus) {
        if (controlType != ControlType.CREDIT) {
            throw new IllegalArgumentException("CreditStatus только для CREDIT");
        }
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;
        this.mark = null;
        this.creditStatus = creditStatus;
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

    public Mark getMark() {
        return mark;
    }

    public CreditStatus getCreditStatus() {
        return creditStatus;
    }

    /**
     * Возвращает числовое представление оценки.
     *
     * @return значение оценки (2-5) или статус зачета (1 - сдан, 0 - не сдан)
     */
    public int getGradeValue() {
        if (mark != null) {
            return mark.getValue();
        }
        if (creditStatus != null) {
            return creditStatus.isPassed() ? 1 : 0;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubjectRecord that = (SubjectRecord) o;
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
        String result = (mark != null) ? mark.getDisplayName()
                : (creditStatus != null ? creditStatus.getDisplayName() : "N/A");
        return semester + ": " + subjectName + " - " + result;
    }
}
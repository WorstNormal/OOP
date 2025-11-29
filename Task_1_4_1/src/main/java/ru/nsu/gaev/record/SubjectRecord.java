package ru.nsu.gaev.record;

import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.CreditStatus;
import ru.nsu.gaev.grade.Mark;

/**
 * Класс, представляющий запись об оценке по предмету в зачетной книжке.
 * Может содержать либо дифференцированную оценку (Mark), либо статус зачета.
 */
public final class SubjectRecord {
    private final String subjectName;
    private final Semester semester;
    private final ControlType controlType;
    private final Mark mark;
    private final CreditStatus creditStatus;

    /**
     * Конструктор записи с дифференцированной оценкой.
     *
     * @param subjectName название предмета
     * @param semester семестр обучения
     * @param controlType тип контроля (экзамен, зачет и т.д.)
     * @param mark дифференцированная оценка
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
     * Конструктор записи со статусом зачета.
     *
     * @param subjectName название предмета
     * @param semester семестр обучения
     * @param controlType тип контроля (должен быть CREDIT)
     * @param creditStatus статус зачета (зачет/не зачет)
     */
    public SubjectRecord(String subjectName, Semester semester,
                         ControlType controlType, CreditStatus creditStatus) {
        if (controlType != ControlType.CREDIT) {
            throw new IllegalArgumentException(
                    "CreditStatus используется только для CREDIT"
            );
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
     * Получить числовое значение оценки.
     * Для дифференцированной оценки возвращает значение Mark,
     * для зачета возвращает 1 (зачет) или 0 (не зачет).
     *

     * @return числовое значение оценки
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
    public String toString() {
        String gradeDisplay;
        if (mark != null) {
            gradeDisplay = mark.getDisplayName();
        } else if (creditStatus != null) {
            gradeDisplay = creditStatus.getDisplayName();
        } else {
            gradeDisplay = "Нет оценки";
        }

        return semester + " | " + subjectName + " ("
                + controlType.getDisplayName() + "): " + gradeDisplay;
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
        return subjectName.equals(that.subjectName)
                && semester.equals(that.semester)
                && controlType == that.controlType;
    }

    @Override
    public int hashCode() {
        return subjectName.hashCode() * 31 + semester.hashCode() * 31
                + controlType.hashCode();
    }
}


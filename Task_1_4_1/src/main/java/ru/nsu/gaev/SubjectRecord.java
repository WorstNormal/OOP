package ru.nsu.gaev;

public class SubjectRecord {
    private String subjectName;
    private int semester;
    private ControlType controlType;
    private int grade; // 0 или 1 для зачетов (незачет/зачет), 2-5 для остальных

    public SubjectRecord(String subjectName, int semester, ControlType controlType, int grade) {
        this.subjectName = subjectName;
        this.semester = semester;
        this.controlType = controlType;
        this.grade = grade;
    }

    public int getSemester() {
        return semester;
    }

    public ControlType getControlType() {
        return controlType;
    }

    public int getGrade() {
        return grade;
    }

    public String getSubjectName() {
        return subjectName;
    }

    @Override
    public String toString() {
        return "Семестр " + semester + " | " + subjectName + " (" + controlType.getDisplayName() + "): " + grade;
    }
}
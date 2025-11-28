package ru.nsu.gaev;

/**
 * Класс, представляющий студента.
 * Содержит информацию о студенте: имя, форму обучения и его зачетную книжку.
 */
public class Student {
    private final String fullName;
    private final String studentId;
    private final boolean isPaidEducation;
    private final ElectronicGradeBook gradeBook;
    private Curriculum curriculum;

    /**
     * Конструктор студента.
     *
     * @param fullName полное имя студента
     * @param studentId номер зачетной книжки
     * @param isPaidEducation true, если обучение платное
     * @param curriculum учебный план студента
     */
    public Student(String fullName, String studentId,
                   boolean isPaidEducation, Curriculum curriculum) {
        this.fullName = fullName;
        this.studentId = studentId;
        this.isPaidEducation = isPaidEducation;
        this.curriculum = curriculum;
        // Передаем учебный план в зачетную книжку,
        // чтобы их состояние было согласовано
        this.gradeBook = new ElectronicGradeBook(isPaidEducation, curriculum);
    }

    /**
     * Альтернативный конструктор без учебного плана.
     *
     * @param fullName полное имя студента
     * @param studentId номер зачетной книжки
     * @param isPaidEducation true, если обучение платное
     */
    public Student(String fullName, String studentId,
                   boolean isPaidEducation) {
        this(fullName, studentId, isPaidEducation, null);
    }

    public String getFullName() {
        return fullName;
    }

    public String getStudentId() {
        return studentId;
    }

    public boolean isPaidEducation() {
        return isPaidEducation;
    }

    public ElectronicGradeBook getGradeBook() {
        return gradeBook;
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    /**
     * Устанавливает учебный план для студента и синхронизирует его
     * с зачетной книжкой.
     *
     * @param curriculum учебный план
     */
    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
        if (this.gradeBook != null) {
            this.gradeBook.setCurriculum(curriculum);
        }
    }

    /**
     * Добавляет запись об оценке в зачетную книжку.
     *
     * @param record запись об оценке
     */
    public void addRecord(SubjectRecord record) {
        gradeBook.addRecord(record);
    }

    /**
     * Возвращает строковое представление студента.
     *
     * @return строка с информацией о студенте
     */
    @Override
    public String toString() {
        return "Студент: " + fullName + " (ID: " + studentId + ")"
                + " | Форма обучения: "
                + (isPaidEducation ? "платная" : "бюджетная")
                + (curriculum != null ? " | " + curriculum : "");
    }
}

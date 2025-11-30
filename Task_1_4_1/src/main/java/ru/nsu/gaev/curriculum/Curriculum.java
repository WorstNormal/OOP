package ru.nsu.gaev.curriculum;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.gaev.record.SubjectRecord;

/**
 * Представляет учебный план.
 *
 * <p>Содержит список обязательных предметов и логику проверки их прохождения.
 */
public final class Curriculum {

    private final String curriculumName;
    private final List<CurriculumSubject> requiredSubjects;

    /**
     * Создает новый учебный план.
     *
     * @param curriculumName название плана
     */
    public Curriculum(String curriculumName) {
        this.curriculumName = curriculumName;
        this.requiredSubjects = new ArrayList<>();
    }

    /**
     * Добавляет обязательный предмет в план.
     *
     * @param subject требуемый предмет
     */
    public void addRequiredSubject(CurriculumSubject subject) {
        requiredSubjects.add(subject);
    }

    /**
     * Возвращает список всех требований.
     *
     * @return список предметов
     */
    public List<CurriculumSubject> getRequiredSubjects() {
        return new ArrayList<>(requiredSubjects);
    }

    /**
     * Проверяет, что в переданном списке записей присутствуют все предметы из плана.
     *
     * @param completedSubjects список сданных предметов
     * @return true, если все обязательные предметы найдены
     */
    public boolean areAllSubjectsCompleted(List<SubjectRecord> completedSubjects) {
        for (CurriculumSubject required : requiredSubjects) {
            boolean found = completedSubjects.stream()
                    .anyMatch(completed ->
                            completed.getSubjectName().equals(required.getSubjectName())
                                    && completed.getSemester().equals(required.getSemester())
                                    && completed.getControlType() == required.getControlType()
                    );
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает название учебного плана.
     *
     * @return название
     */
    public String getCurriculumName() {
        return curriculumName;
    }
}
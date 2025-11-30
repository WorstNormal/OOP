package ru.nsu.gaev.curriculum;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.gaev.record.SubjectRecord;

/**
 * Класс, представляющий учебный план студента.
 * Содержит информацию о том, какие предметы и в каких семестрах должны быть
 * пройдены.
 */
public final class Curriculum {
    private final String curriculumName;
    private final List<CurriculumSubject> requiredSubjects;

    /**
     * Конструктор учебного плана.
     *
     * @param curriculumName название учебного плана
     */
    public Curriculum(String curriculumName) {
        this.curriculumName = curriculumName;
        this.requiredSubjects = new ArrayList<>();
    }

    /**
     * Добавляет требуемый предмет в учебный план.
     *
     * @param subject требуемый предмет
     */
    public void addRequiredSubject(CurriculumSubject subject) {
        requiredSubjects.add(subject);
    }

    /**
     * Возвращает список всех требуемых предметов.
     *
     * @return список требуемых предметов
     */
    public List<CurriculumSubject> getRequiredSubjects() {
        return new ArrayList<>(requiredSubjects);
    }

    /**
     * Проверяет, прошёл ли студент все требуемые предметы.
     *
     * @param completedSubjects список пройденных предметов (SubjectRecord)
     * @return true, если все требуемые предметы пройдены
     */
    public boolean areAllSubjectsCompleted(
            List<SubjectRecord> completedSubjects) {
        for (CurriculumSubject required : requiredSubjects) {
            boolean found = completedSubjects.stream()
                    .anyMatch(completed ->
                            completed.getSubjectName()
                                    .equals(required.getSubjectName())
                                    && completed.getSemester()
                                    .equals(required.getSemester())
                                    && completed.getControlType()
                                    == required.getControlType()
                    );
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Возвращает список недостающих предметов относительно пройденных.
     *
     * @param completedSubjects список пройденных предметов
     * @return список требуемых предметов, которые ещё не пройдены
     */
    public List<CurriculumSubject> getMissingSubjects(
            List<SubjectRecord> completedSubjects) {
        List<CurriculumSubject> missing = new ArrayList<>();
        for (CurriculumSubject required : requiredSubjects) {
            boolean found = completedSubjects.stream()
                    .anyMatch(completed ->
                            completed.getSubjectName()
                                    .equals(required.getSubjectName())
                                    && completed.getSemester()
                                    .equals(required.getSemester())
                                    && completed.getControlType()
                                    == required.getControlType()
                    );
            if (!found) {
                missing.add(required);
            }
        }
        return missing;
    }

    public String getCurriculumName() {
        return curriculumName;
    }

    @Override
    public String toString() {
        return "Учебный план: " + curriculumName + " ("
                + requiredSubjects.size() + " предметов)";
    }
}
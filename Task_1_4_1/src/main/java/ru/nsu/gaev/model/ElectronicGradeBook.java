package ru.nsu.gaev.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import ru.nsu.gaev.curriculum.ControlType;
import ru.nsu.gaev.curriculum.Curriculum;
import ru.nsu.gaev.curriculum.Semester;
import ru.nsu.gaev.grade.Mark;
import ru.nsu.gaev.record.SubjectRecord;

/**
 * Класс электронной зачетной книжки.
 * Хранит список сданных предметов и содержит логику вычисления
 * среднего балла, возможности получения стипендии и красного диплома.
 */
public final class ElectronicGradeBook {
    private final List<SubjectRecord> records;
    private final boolean isPaidEducation;
    private final Semester currentSemester;
    private Curriculum curriculum;

    /**
     * Конструктор зачетной книжки.
     *
     * @param isPaidEducation true, если обучение платное.
     * @param currentSemester текущий семестр обучения.
     */
    public ElectronicGradeBook(boolean isPaidEducation,
                               Semester currentSemester) {
        this.records = new ArrayList<>();
        this.isPaidEducation = isPaidEducation;
        this.currentSemester = currentSemester;
        this.curriculum = null;
    }

    /**
     * Конструктор зачетной книжки с учебным планом.
     *
     * @param isPaidEducation true, если обучение платное.
     * @param currentSemester текущий семестр обучения.
     * @param curriculum учебный план студента.
     */
    public ElectronicGradeBook(boolean isPaidEducation,
                               Semester currentSemester,
                               Curriculum curriculum) {
        this.records = new ArrayList<>();
        this.isPaidEducation = isPaidEducation;
        this.currentSemester = currentSemester;
        this.curriculum = curriculum;
    }

    public void addRecord(SubjectRecord record) {
        this.records.add(record);
    }

    public List<SubjectRecord> getRecords() {
        return new ArrayList<>(records);
    }

    public Curriculum getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    public Semester getCurrentSemester() {
        return currentSemester;
    }

    public boolean isPaidEducation() {
        return isPaidEducation;
    }

    /**
     * Вычисляет текущий средний балл за все время обучения.
     * Учитываются только дифференцированные оценки (не зачеты).
     *
     * @return Средний балл или 0.0, если оценок нет.
     */
    public double calculateAverageGrade() {
        List<SubjectRecord> gradedRecords = records.stream()
                .filter(r -> r.getMark() != null)
                .collect(Collectors.toList());

        if (gradedRecords.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (SubjectRecord r : gradedRecords) {
            sum += r.getMark().getValue();
        }
        return sum / gradedRecords.size();
    }

    /**
     * Проверяет возможность перевода с платного на бюджет.
     * Требование: отсутствие оценок "удовлетворительно" (и ниже) за экзамены
     * за последние две экзаменационные сессии (текущую и предыдущую).
     *
     * @return true, если перевод возможен.
     */
    public boolean canTransferToBudget() {
        if (!isPaidEducation) {
            return true;
        }

        Semester previousSemester = currentSemester.previous().orElse(null);

        List<SubjectRecord> lastTwoSessions = records.stream()
                .filter(r -> r.getSemester().equals(currentSemester)
                        || r.getSemester().equals(previousSemester))
                .collect(Collectors.toList());

        if (lastTwoSessions.isEmpty()) {
            return false;
        }

        for (SubjectRecord r : lastTwoSessions) {
            if (r.getControlType() == ControlType.EXAM && r.getMark() != null) {
                if (r.getMark().getValue() <= Mark.SATISFACTORY.getValue()) {
                    return false;
                }
            }
            if (r.getMark() == null && r.getCreditStatus() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет возможность получения красного диплома (с отличием).
     * Требования:
     * - 75% оценок "отлично" (5) в приложении к диплому (без учета ВКР);
     * - отсутствие итоговых оценок "удовлетворительно" или "неудовлетворительно";
     * - квалификационная работа (THESIS) на "отлично";
     * - все предметы из учебного плана должны быть пройдены.
     *
     * @return true, если условия выполняются.
     */
    public boolean canGetRedDiploma() {
        if (curriculum != null) {
            if (!curriculum.areAllSubjectsCompleted(records)) {
                return false;
            }
        }

        List<SubjectRecord> diplomaRecords = records.stream()
                .filter(r -> r.getMark() != null)
                .collect(Collectors.toList());

        if (diplomaRecords.isEmpty()) {
            return false;
        }

        boolean hasThesisExcellent = diplomaRecords.stream()
                .filter(r -> r.getControlType() == ControlType.THESIS)
                .anyMatch(r -> r.getMark().getValue() == Mark.EXCELLENT.getValue());

        if (!hasThesisExcellent) {
            return false;
        }

        int excellentCount = 0;
        int totalCount = 0;

        for (SubjectRecord r : diplomaRecords) {
            if (r.getControlType() == ControlType.THESIS) {
                continue;
            }

            totalCount++;
            int markValue = r.getMark().getValue();

            if (markValue <= Mark.SATISFACTORY.getValue()) {
                return false;
            }

            if (markValue == Mark.EXCELLENT.getValue()) {
                excellentCount++;
            }
        }

        if (totalCount == 0) {
            return false;
        }

        double excellentPercentage = (double) excellentCount / totalCount;
        return excellentPercentage >= 0.75;
    }

    /**
     * Проверяет возможность получения повышенной стипендии в текущем семестре.
     * Условие: все оценки за текущий семестр - "отлично" (5),
     * все зачеты - сданы.
     *
     * @return true, если положена повышенная стипендия.
     */
    public boolean canGetIncreasedScholarship() {
        List<SubjectRecord> currentSession = records.stream()
                .filter(r -> r.getSemester().equals(currentSemester))
                .collect(Collectors.toList());

        if (currentSession.isEmpty()) {
            return false;
        }

        for (SubjectRecord r : currentSession) {
            if (r.getMark() != null) {
                if (r.getMark().getValue() != Mark.EXCELLENT.getValue()) {
                    return false;
                }
            }
            if (r.getCreditStatus() != null) {
                if (!r.getCreditStatus().isPassed()) {
                    return false;
                }
            }
        }
        return true;
    }
}
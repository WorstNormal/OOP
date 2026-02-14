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
 * Электронная зачетная книжка.
 *
 * <p>Отвечает за хранение оценок и расчет академических показателей:
 * среднего балла, стипендии, красного диплома.
 */
public final class ElectronicGradeBook {

    private final List<SubjectRecord> records;
    private final boolean isPaidEducation;
    private final Semester currentSemester;
    private Curriculum curriculum;

    /**
     * Конструктор без начального учебного плана.
     *
     * @param isPaidEducation платное ли обучение
     * @param currentSemester текущий семестр
     */
    public ElectronicGradeBook(boolean isPaidEducation, Semester currentSemester) {
        this(isPaidEducation, currentSemester, null);
    }

    /**
     * Полный конструктор.
     *
     * @param isPaidEducation платное ли обучение
     * @param currentSemester текущий семестр
     * @param curriculum учебный план
     */
    public ElectronicGradeBook(boolean isPaidEducation,
                               Semester currentSemester,
                               Curriculum curriculum) {
        this.records = new ArrayList<>();
        this.isPaidEducation = isPaidEducation;
        this.currentSemester = currentSemester;
        this.curriculum = curriculum;
    }

    /**
     * Возвращает список всех записей в зачетной книжке. Возвращает копию списка для безопасности.
     */
    public List<SubjectRecord> getRecords() {
        return new ArrayList<>(records);
    }

    /**
     * Возвращает текущий семестр обучения.
     */
    public Semester getCurrentSemester() {
        return currentSemester;
    }

    /**
     * Возвращает true, если обучение платное.
     */
    public boolean isPaidEducation() {
        return isPaidEducation;
    }

    /**
     * Возвращает текущий учебный план.
     */
    public Curriculum getCurriculum() {
        return curriculum;
    }

    /**
     * Добавляет запись в зачетку.
     *
     * @param record запись предмета
     */
    public void addRecord(SubjectRecord record) {
        records.add(record);
    }

    /**
     * Устанавливает или обновляет учебный план.
     *
     * @param curriculum новый план
     */
    public void setCurriculum(Curriculum curriculum) {
        this.curriculum = curriculum;
    }

    /**
     * Вычисляет средний балл (только дифференцированные оценки).
     *
     * @return средний балл
     */
    public double calculateAverageGrade() {
        List<SubjectRecord> gradedRecords = records.stream()
                .filter(r -> r.getMark() != null)
                .collect(Collectors.toList());

        if (gradedRecords.isEmpty()) {
            return 0.0;
        }

        double sum = gradedRecords.stream()
                .mapToInt(r -> r.getMark().getValue())
                .sum();

        return sum / gradedRecords.size();
    }

    /**
     * Проверяет возможность перевода на бюджет.
     *
     * <p>Условие: отсутствие троек за экзамены в двух последних сессиях.
     *
     * @return true если перевод возможен
     */
    public boolean canTransferToBudget() {
        if (!isPaidEducation) {
            return true;
        }

        Semester prev1 = currentSemester.previous().orElse(null);
        Semester prev2 = (prev1 != null) ? prev1.previous().orElse(null) : null;

        if (prev1 == null) {
            return false;
        }

        List<SubjectRecord> lastSessionRecords = records.stream()
                .filter(r -> r.getSemester().equals(prev1)
                        || (prev2 != null && r.getSemester().equals(prev2)))
                .collect(Collectors.toList());

        if (lastSessionRecords.isEmpty()) {
            return false;
        }

        for (SubjectRecord r : lastSessionRecords) {
            if (r.getControlType() == ControlType.EXAM && r.getMark() != null) {
                if (r.getMark().getValue() <= Mark.SATISFACTORY.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Проверяет возможность получения красного диплома.
     *
     * <p>Условия: выполнение плана, 75% отлично, нет троек, ВКР на 5.
     *
     * @return true если условия выполнены
     */
    public boolean canGetRedDiploma() {
        if (curriculum != null && !curriculum.areAllSubjectsCompleted(records)) {
            return false;
        }

        List<SubjectRecord> diplomaRecords = records.stream()
                .filter(r -> r.getMark() != null)
                .collect(Collectors.toList());

        if (diplomaRecords.isEmpty()) {
            return false;
        }

        boolean thesisExcellent = diplomaRecords.stream()
                .filter(r -> r.getControlType() == ControlType.THESIS)
                .anyMatch(r -> r.getMark() == Mark.EXCELLENT);

        if (!thesisExcellent) {
            return false;
        }

        long totalGraded = 0;
        long excellentCount = 0;

        for (SubjectRecord r : diplomaRecords) {
            if (r.getControlType() == ControlType.THESIS) {
                continue;
            }
            totalGraded++;
            int val = r.getMark().getValue();

            if (val <= Mark.SATISFACTORY.getValue()) {
                return false;
            }
            if (val == Mark.EXCELLENT.getValue()) {
                excellentCount++;
            }
        }

        if (totalGraded == 0) {
            return false;
        }

        return ((double) excellentCount / totalGraded) >= 0.75;
    }

    /**
     * Проверяет право на повышенную стипендию.
     *
     * <p>Условие: прошлая сессия закрыта только на отлично.
     *
     * @return true если положена стипендия
     */
    public boolean canGetIncreasedScholarship() {
        Semester prev = currentSemester.previous().orElse(null);
        if (prev == null) {
            return false;
        }

        List<SubjectRecord> prevSession = records.stream()
                .filter(r -> r.getSemester().equals(prev))
                .collect(Collectors.toList());

        if (prevSession.isEmpty()) {
            return false;
        }

        for (SubjectRecord r : prevSession) {
            if (r.getMark() != null && r.getMark() != Mark.EXCELLENT) {
                return false;
            }
            if (r.getCreditStatus() != null && !r.getCreditStatus().isPassed()) {
                return false;
            }
        }
        return true;
    }
}
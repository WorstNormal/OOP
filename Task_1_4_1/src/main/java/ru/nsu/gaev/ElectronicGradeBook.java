package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс электронной зачетной книжки.
 * Хранит список сданных предметов и содержит логику вычисления
 * среднего балла, возможности получения стипендии и красного диплома.
 */
public class ElectronicGradeBook {
    private List<SubjectRecord> records;
    private boolean isPaidEducation; // Платное обучение или нет
    private int currentSemester;

    /**
     * Конструктор зачетной книжки.
     *
     * @param isPaidEducation true, если обучение платное.
     * @param currentSemester текущий семестр обучения.
     */
    public ElectronicGradeBook(boolean isPaidEducation, int currentSemester) {
        this.records = new ArrayList<>();
        this.isPaidEducation = isPaidEducation;
        this.currentSemester = currentSemester;
    }

    public void addRecord(SubjectRecord record) {
        this.records.add(record);
    }

    /**
     * Вычисляет текущий средний балл за все время обучения.
     * Учитываются только дифференцированные оценки.
     *
     * @return Средний балл или 0.0, если оценок нет.
     */
    public double calculateAverageGrade() {
        List<SubjectRecord> gradedRecords = records.stream()
                .filter(r -> r.getControlType() != ControlType.CREDIT && r.getGrade() > 1)
                .collect(Collectors.toList());

        if (gradedRecords.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (SubjectRecord r : gradedRecords) {
            sum += r.getGrade();
        }
        return sum / gradedRecords.size();
    }

    /**
     * Проверяет возможность перевода с платного на бюджет.
     * Условие: отсутствие троек за экзамены за последние две сессии.
     *
     * @return true, если перевод возможен.
     */
    public boolean canTransferToBudget() {
        if (!isPaidEducation) {
            return true; // Уже на бюджете
        }

        int previousSemester = currentSemester - 1;
        if (previousSemester < 1) {
            return false;
        }

        List<SubjectRecord> lastTwoSessions = records.stream()
                .filter(r -> r.getSemester() == currentSemester
                        || r.getSemester() == previousSemester)
                .collect(Collectors.toList());

        if (lastTwoSessions.isEmpty()) {
            return false;
        }

        for (SubjectRecord r : lastTwoSessions) {
            if (r.getControlType() == ControlType.EXAM && r.getGrade() == 3) {
                return false;
            }
            if (r.getGrade() < 3) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет возможность получения красного диплома (с отличием).
     * Требования: 75% оценок "отлично", отсутствие троек, ВКР на "отлично".
     *
     * @return true, если условия выполняются (прогноз).
     */
    public boolean isRedDiplomaLikely() {
        List<SubjectRecord> diplomaRecords = records.stream()
                .filter(r -> r.getControlType() != ControlType.CREDIT)
                .collect(Collectors.toList());

        if (diplomaRecords.isEmpty()) {
            return false;
        }

        int excellentCount = 0;
        boolean hasThree = false;
        boolean thesisIsExcellent = true;

        for (SubjectRecord r : diplomaRecords) {
            if (r.getGrade() == 5) {
                excellentCount++;
            }
            if (r.getGrade() == 3) {
                hasThree = true;
            }
            if (r.getControlType() == ControlType.THESIS) {
                if (r.getGrade() < 5) {
                    thesisIsExcellent = false;
                }
            }
        }

        if (hasThree) {
            return false;
        }
        if (!thesisIsExcellent) {
            return false;
        }

        double excellentPercentage = (double) excellentCount / diplomaRecords.size();
        return excellentPercentage >= 0.75;
    }

    /**
     * Проверяет возможность получения повышенной стипендии в текущем семестре.
     * Условие: все оценки за текущий семестр - "отлично".
     *
     * @return true, если положена повышенная стипендия.
     */
    public boolean canGetIncreasedScholarship() {
        List<SubjectRecord> currentSession = records.stream()
                .filter(r -> r.getSemester() == currentSemester)
                .collect(Collectors.toList());

        if (currentSession.isEmpty()) {
            return false;
        }

        for (SubjectRecord r : currentSession) {
            if (r.getControlType() != ControlType.CREDIT) {
                if (r.getGrade() < 5) {
                    return false;
                }
            }
            if (r.getControlType() == ControlType.CREDIT && r.getGrade() == 0) {
                return false;
            }
        }
        return true;
    }
}
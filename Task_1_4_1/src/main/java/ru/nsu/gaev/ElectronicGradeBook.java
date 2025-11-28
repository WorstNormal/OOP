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
    private final List<SubjectRecord> records;
    private final boolean isPaidEducation;
    private Curriculum curriculum;

    /**
     * Конструктор зачетной книжки.
     *
     * @param isPaidEducation true, если обучение платное.
     */
    public ElectronicGradeBook(boolean isPaidEducation) {
        this.records = new ArrayList<>();
        this.isPaidEducation = isPaidEducation;
        this.curriculum = null;
    }

    /**
     * Конструктор зачетной книжки с учебным планом.
     *
     * @param isPaidEducation true, если обучение платное.
     * @param curriculum учебный план студента.
     */
    public ElectronicGradeBook(boolean isPaidEducation, Curriculum curriculum) {
        this.records = new ArrayList<>();
        this.isPaidEducation = isPaidEducation;
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

    /**
     * Вычисляет текущий средний балл за все время обучения.
     * Учитываются только дифференцированные оценки.
     *
     * @return Средний балл или 0.0, если оценок нет.
     */
    public double calculateAverageGrade() {
        List<SubjectRecord> gradedRecords = records.stream()
                .filter(r -> r.getControlType() != ControlType.CREDIT && r.getGradeValue() > 1)
                .collect(Collectors.toList());

        if (gradedRecords.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (SubjectRecord r : gradedRecords) {
            sum += r.getGradeValue();
        }
        return sum / gradedRecords.size();
    }

    /**
     * Проверяет возможность перевода с платного на бюджет.
     * Условие: отсутствие троек за экзамены за последние две сессии.
     *
     * @param currentSemester текущий номер семестра.
     * @return true, если перевод возможен.
     */
    public boolean canTransferToBudget(int currentSemester) {
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
            if (r.getControlType() == ControlType.EXAM && r.getGradeValue() == 3) {
                return false;
            }
            if (r.getGradeValue() < 3) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет возможность получения красного диплома (с отличием).
     * Требования: 75% оценок "отлично", отсутствие троек, ВКР на "отлично",
     * и все предметы из учебного плана должны быть пройдены.
     *
     * @return true, если условия выполняются.
     */
    public boolean canGetRedDiploma() {
        // Проверка учебного плана
        if (curriculum != null) {
            if (!curriculum.areAllSubjectsCompleted(records)) {
                return false;
            }
        }

        // Проверка оценок
        List<SubjectRecord> diplomaRecords = records.stream()
                .filter(r -> r.getControlType() != ControlType.CREDIT)
                .collect(Collectors.toList());

        if (diplomaRecords.isEmpty()) {
            return false;
        }

        int excellentCount = 0;
        boolean hasThree = false;
        boolean thesisFound = false; // теперь ВКР обязателен

        for (SubjectRecord r : diplomaRecords) {
            if (r.getGradeValue() == 5) {
                excellentCount++;
            }
            if (r.getGradeValue() == 3) {
                hasThree = true;
            }
            if (r.getControlType() == ControlType.THESIS) {
                thesisFound = true;
                if (r.getGradeValue() < 5) {
                    // ВКР есть, но оценка менее 5 — сразу fail
                    return false;
                }
            }
        }

        if (hasThree) {
            return false;
        }

        // Требуем, чтобы ВКР был обязательно найден и оценен на 5
        if (!thesisFound) {
            return false;
        }

        double excellentPercentage = (double) excellentCount / diplomaRecords.size();
        return excellentPercentage >= 0.75;
    }

    /**
     * Проверяет возможность получения повышенной стипендии в текущем семестре.
     * Условие: все оценки за текущий семестр - "отлично".
     *
     * @param currentSemester текущий номер семестра.
     * @return true, если положена повышенная стипендия.
     */
    public boolean canGetIncreasedScholarship(int currentSemester) {
        List<SubjectRecord> currentSession = records.stream()
                .filter(r -> r.getSemester() == currentSemester)
                .collect(Collectors.toList());

        if (currentSession.isEmpty()) {
            return false;
        }

        for (SubjectRecord r : currentSession) {
            if (r.getControlType() != ControlType.CREDIT) {
                if (r.getGradeValue() < 5) {
                    return false;
                }
            }
            if (r.getControlType() == ControlType.CREDIT) {
                CreditGrade creditGrade = (CreditGrade) r.getGrade();
                if (!creditGrade.isPassed()) {
                    return false;
                }
            }
        }
        return true;
    }
}
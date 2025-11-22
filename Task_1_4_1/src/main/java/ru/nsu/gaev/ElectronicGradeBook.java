package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElectronicGradeBook {
    private List<SubjectRecord> records;
    private boolean isPaidEducation; // Платное обучение или нет
    private int currentSemester;

    public ElectronicGradeBook(boolean isPaidEducation, int currentSemester) {
        this.records = new ArrayList<>();
        this.isPaidEducation = isPaidEducation;
        this.currentSemester = currentSemester;
    }

    public void addRecord(SubjectRecord record) {
        this.records.add(record);
    }

    // 1. Текущий средний балл за все время
    public double calculateAverageGrade() {
        // Считаем только те предметы, где есть оценка (экзамены, диф.зачеты, курсовые)
        // Простые зачеты (где оценка по сути pass/fail) обычно в среднем балле не участвуют,
        // если они не дифференцированные. Будем считать оценки > 1.
        List<SubjectRecord> gradedRecords = records.stream()
                .filter(r -> r.getControlType() != ControlType.CREDIT && r.getGrade() > 1)
                .collect(Collectors.toList());

        if (gradedRecords.isEmpty()) return 0.0;

        double sum = 0;
        for (SubjectRecord r : gradedRecords) {
            sum += r.getGrade();
        }
        return sum / gradedRecords.size();
    }

    // 2. Возможность перевода на бюджет
    public boolean canTransferToBudget() {
        if (!isPaidEducation) return true; // Уже на бюджете

        // Нужно проверить последние две сессии (текущий семестр и предыдущий)
        int previousSemester = currentSemester - 1;
        if (previousSemester < 1) return false; // Недостаточно данных

        List<SubjectRecord> lastTwoSessions = records.stream()
                .filter(r -> r.getSemester() == currentSemester || r.getSemester() == previousSemester)
                .collect(Collectors.toList());

        if (lastTwoSessions.isEmpty()) return false;

        for (SubjectRecord r : lastTwoSessions) {
            // Условие: отсутствие троек (3) за ЭКЗАМЕНЫ.
            // В диф. зачетах тройки допустимы (согласно заданию).
            if (r.getControlType() == ControlType.EXAM && r.getGrade() == 3) {
                return false;
            }
            // Конечно, "двойки" недопустимы нигде для перевода
            if (r.getGrade() < 3) {
                return false;
            }
        }
        return true;
    }

    // 3. Возможность получения красного диплома (прогноз)
    public boolean isRedDiplomaLikely() {
        // Фильтруем оценки, идущие в диплом (Экзамены, Диф.зачеты, Практики, Курсовые)
        // Исключаем обычные зачеты
        List<SubjectRecord> diplomaRecords = records.stream()
                .filter(r -> r.getControlType() != ControlType.CREDIT)
                .collect(Collectors.toList());

        if (diplomaRecords.isEmpty()) return false; // Пока нет оценок

        int excellentCount = 0;
        boolean hasThree = false;
        boolean thesisIsExcellent = true; // По умолчанию true (если еще не сдавал), если сдал - проверим

        for (SubjectRecord r : diplomaRecords) {
            if (r.getGrade() == 5) {
                excellentCount++;
            }
            // Условие: отсутствие итоговых оценок "удовлетворительно" (3)
            // Это касается и Экзаменов, и Диф. зачетов
            if (r.getGrade() == 3) {
                hasThree = true;
            }

            // Проверка ВКР
            if (r.getControlType() == ControlType.THESIS) {
                if (r.getGrade() < 5) thesisIsExcellent = false;
            }
        }

        if (hasThree) return false;
        if (!thesisIsExcellent) return false;

        double excellentPercentage = (double) excellentCount / diplomaRecords.size();

        // Условие: 75% оценок - отлично
        return excellentPercentage >= 0.75;
    }

    // 4. Повышенная стипендия в текущем семестре
    public boolean canGetIncreasedScholarship() {
        // Обычно стипендия назначается по результатам ПОСЛЕДНЕЙ закрытой сессии.
        // Предположим, мы проверяем результаты currentSemester.

        List<SubjectRecord> currentSession = records.stream()
                .filter(r -> r.getSemester() == currentSemester)
                .collect(Collectors.toList());

        if (currentSession.isEmpty()) return false;

        // Логика повышенной стипендии: обычно это "Только на отлично".
        // В задании не уточнены критерии, используем стандартный строгий критерий.
        for (SubjectRecord r : currentSession) {
            // Проверяем только дифференцируемые оценки
            if (r.getControlType() != ControlType.CREDIT) {
                if (r.getGrade() < 5) return false;
            }
            // Если обычный зачет не сдан, стипендии тоже нет
            if (r.getControlType() == ControlType.CREDIT && r.getGrade() == 0) return false;
        }
        return true;
    }
}
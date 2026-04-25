package ru.nsu.gaev.oopchecker.domain;

import java.time.LocalDate;

public record ActivitySettings(
        LocalDate semesterStart,
        int totalWeeks,
        double weight
) {
}

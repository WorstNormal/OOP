package ru.nsu.gaev.oopchecker.domain;

import java.time.LocalDate;

public record TaskSpec(
        String id,
        String title,
        int maxScore,
        LocalDate softDeadline,
        LocalDate hardDeadline
) {
}

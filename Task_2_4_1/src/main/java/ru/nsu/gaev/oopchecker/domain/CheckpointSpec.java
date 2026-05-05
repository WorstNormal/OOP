package ru.nsu.gaev.oopchecker.domain;

import java.time.LocalDate;

public record CheckpointSpec(
        String name,
        LocalDate date
) {
}

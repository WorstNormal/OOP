package ru.nsu.gaev.oopchecker.domain;

import java.util.List;

public record GroupSpec(
        String name,
        List<StudentSpec> students
) {
}

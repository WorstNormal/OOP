package ru.nsu.gaev.oopchecker.engine;

import java.util.List;

public record GroupReport(
        String groupName,
        List<StudentReport> students
) {
}

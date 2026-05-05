package ru.nsu.gaev.oopchecker.engine;

import java.util.List;

import ru.nsu.gaev.oopchecker.domain.CourseConfig;

public record CourseRunReport(
        CourseConfig config,
        List<GroupReport> groups
) {
}

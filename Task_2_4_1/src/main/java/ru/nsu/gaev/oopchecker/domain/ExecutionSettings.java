package ru.nsu.gaev.oopchecker.domain;

import java.time.Duration;
import java.util.List;

public record ExecutionSettings(
        Duration timeout,
        String compileCommand,
        String docsCommand,
        String styleCommand,
        String testCommand,
        List<String> preferredBranches
) {
}

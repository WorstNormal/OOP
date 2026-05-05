package ru.nsu.gaev.oopchecker.engine;

public record TestCounts(
        int passed,
        int failed,
        int skipped
) {
}

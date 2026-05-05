package ru.nsu.gaev.oopchecker.engine;

public record TaskRunResult(
        String taskId,
        String taskTitle,
        boolean compiled,
        boolean docsGenerated,
        boolean stylePassed,
        TestCounts testCounts,
        double bonusPoints,
        double score,
        String branch,
        String repositoryPath,
        String commandOutput
) {
}

package ru.nsu.gaev.oopchecker.engine;

import java.util.List;

public record StudentReport(
        String groupName,
        String studentName,
        String github,
        String repositoryPath,
        List<TaskRunResult> taskResults,
        List<CheckpointGrade> checkpointGrades,
        double rawPoints,
        double activityPercent,
        double finalPoints,
        String finalGrade
) {
}

package ru.nsu.gaev.oopchecker.domain;

public record BonusSpec(
        String groupName,
        String studentGithub,
        String taskId,
        double points,
        String reason
) {
}

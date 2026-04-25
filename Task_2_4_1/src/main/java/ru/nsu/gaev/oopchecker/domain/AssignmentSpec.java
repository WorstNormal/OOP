package ru.nsu.gaev.oopchecker.domain;

import java.util.List;

public record AssignmentSpec(
        String groupName,
        String studentGithub,
        List<String> taskIds
) {
}

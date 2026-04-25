package ru.nsu.gaev.oopchecker.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record CourseConfig(
        List<TaskSpec> tasks,
        List<GroupSpec> groups,
        List<AssignmentSpec> assignments,
        List<CheckpointSpec> checkpoints,
        List<GradeBoundary> gradeBoundaries,
        ActivitySettings activitySettings,
        ExecutionSettings executionSettings,
        List<BonusSpec> bonuses
) {
    public static Builder builder() {
        return new Builder();
    }

    public Map<String, TaskSpec> tasksById() {
        Map<String, TaskSpec> result = new LinkedHashMap<>();
        for (TaskSpec task : tasks) {
            result.put(task.id(), task);
        }
        return Collections.unmodifiableMap(result);
    }

    public Map<String, GroupSpec> groupsByName() {
        Map<String, GroupSpec> result = new LinkedHashMap<>();
        for (GroupSpec group : groups) {
            result.put(group.name(), group);
        }
        return Collections.unmodifiableMap(result);
    }

    public static final class Builder {
        private final List<TaskSpec> tasks = new ArrayList<>();
        private final List<GroupSpec> groups = new ArrayList<>();
        private final List<AssignmentSpec> assignments = new ArrayList<>();
        private final List<CheckpointSpec> checkpoints = new ArrayList<>();
        private final List<GradeBoundary> gradeBoundaries = new ArrayList<>();
        private final List<BonusSpec> bonuses = new ArrayList<>();
        private ActivitySettings activitySettings = defaultActivitySettings();
        private ExecutionSettings executionSettings = defaultExecutionSettings();

        public Builder() {
            gradeBoundaries.add(new GradeBoundary(90, "5"));
            gradeBoundaries.add(new GradeBoundary(75, "4"));
            gradeBoundaries.add(new GradeBoundary(60, "3"));
            gradeBoundaries.add(new GradeBoundary(0, "2"));
        }

        public Builder addTask(TaskSpec task) {
            tasks.add(task);
            return this;
        }

        public Builder addGroup(GroupSpec group) {
            groups.add(group);
            return this;
        }

        public Builder addAssignment(AssignmentSpec assignment) {
            assignments.add(assignment);
            return this;
        }

        public Builder addCheckpoint(CheckpointSpec checkpoint) {
            checkpoints.add(checkpoint);
            return this;
        }

        public Builder addBoundary(GradeBoundary boundary) {
            gradeBoundaries.add(boundary);
            return this;
        }

        public Builder addBonus(BonusSpec bonus) {
            bonuses.add(bonus);
            return this;
        }

        public Builder activitySettings(ActivitySettings activitySettings) {
            this.activitySettings = activitySettings;
            return this;
        }

        public Builder executionSettings(ExecutionSettings executionSettings) {
            this.executionSettings = executionSettings;
            return this;
        }

        public ExecutionSettings executionSettings() {
            return executionSettings;
        }

        public Builder merge(CourseConfig config) {
            tasks.addAll(config.tasks());
            groups.addAll(config.groups());
            assignments.addAll(config.assignments());
            checkpoints.addAll(config.checkpoints());
            gradeBoundaries.addAll(config.gradeBoundaries());
            bonuses.addAll(config.bonuses());
            if (config.activitySettings() != null) {
                activitySettings = config.activitySettings();
            }
            if (config.executionSettings() != null) {
                executionSettings = config.executionSettings();
            }
            return this;
        }

        public CourseConfig build() {
            return new CourseConfig(
                    List.copyOf(tasks),
                    List.copyOf(groups),
                    List.copyOf(assignments),
                    List.copyOf(checkpoints),
                    List.copyOf(gradeBoundaries),
                    activitySettings,
                    executionSettings,
                    List.copyOf(bonuses)
            );
        }

        private static ActivitySettings defaultActivitySettings() {
            return new ActivitySettings(null, 0, 0.0);
        }

        private static ExecutionSettings defaultExecutionSettings() {
            return new ExecutionSettings(
                    java.time.Duration.ofMinutes(10),
                    "gradlew.bat compileJava",
                    "gradlew.bat javadoc",
                    "gradlew.bat checkstyleMain",
                    "gradlew.bat test",
                    List.of("main", "master")
            );
        }
    }
}

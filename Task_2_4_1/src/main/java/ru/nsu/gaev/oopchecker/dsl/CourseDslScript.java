package ru.nsu.gaev.oopchecker.dsl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import groovy.lang.Closure;
import groovy.lang.Script;
import ru.nsu.gaev.oopchecker.domain.ActivitySettings;
import ru.nsu.gaev.oopchecker.domain.AssignmentSpec;
import ru.nsu.gaev.oopchecker.domain.BonusSpec;
import ru.nsu.gaev.oopchecker.domain.CheckpointSpec;
import ru.nsu.gaev.oopchecker.domain.CourseConfig;
import ru.nsu.gaev.oopchecker.domain.ExecutionSettings;
import ru.nsu.gaev.oopchecker.domain.GradeBoundary;
import ru.nsu.gaev.oopchecker.domain.GroupSpec;
import ru.nsu.gaev.oopchecker.domain.StudentSpec;
import ru.nsu.gaev.oopchecker.domain.TaskSpec;

public abstract class CourseDslScript extends Script {
    private final CourseConfig.Builder builder = CourseConfig.builder();
    private DslConfigLoader loader;
    private Path sourceFile;

    public void setLoader(DslConfigLoader loader) {
        this.loader = loader;
    }

    public void setSourceFile(Path sourceFile) {
        this.sourceFile = sourceFile;
    }

    public CourseConfig getConfig() {
        return builder.build();
    }

    public void include(String relativePath) throws IOException {
        builder.merge(loader.loadImported(sourceFile, relativePath));
    }

    public void tasks(Closure<?> closure) {
        runClosure(closure, new TasksBlock());
    }

    public void groups(Closure<?> closure) {
        runClosure(closure, new GroupsBlock());
    }

    public void checks(Closure<?> closure) {
        runClosure(closure, new ChecksBlock());
    }

    public void checkpoints(Closure<?> closure) {
        runClosure(closure, new CheckpointsBlock());
    }

    public void settings(Closure<?> closure) {
        runClosure(closure, new SettingsBlock());
    }

    private void runClosure(Closure<?> closure, Object delegate) {
        Closure<?> configured = (Closure<?>) closure.rehydrate(delegate, this, this);
        configured.setResolveStrategy(Closure.DELEGATE_FIRST);
        configured.call();
    }

    private final class TasksBlock {
        public void task(Map<?, ?> attributes) {
            String title = attributes.containsKey("title") ? stringValue(attributes, "title") : stringValue(attributes, "name");
            builder.addTask(new TaskSpec(
                    stringValue(attributes, "id"),
                    title,
                    intValue(attributes, "maxScore", intValue(attributes, "points", 0)),
                    dateValue(attributes, "softDeadline"),
                    dateValue(attributes, "hardDeadline")
            ));
        }
    }

    private final class GroupsBlock {
        public void group(Map<?, ?> attributes, Closure<?> closure) {
            GroupBuilder groupBuilder = new GroupBuilder(stringValue(attributes, "name"));
            if (closure != null) {
                runClosure(closure, groupBuilder);
            }
            builder.addGroup(groupBuilder.build());
        }
    }

    private final class ChecksBlock {
        public void check(Map<?, ?> attributes) {
            String github = attributes.containsKey("student") ? stringValue(attributes, "student") : stringValue(attributes, "github");
            builder.addAssignment(new AssignmentSpec(
                    stringValue(attributes, "group"),
                    github,
                    toStringList(attributes.get("tasks"))
            ));
        }
    }

    private final class CheckpointsBlock {
        public void checkpoint(Map<?, ?> attributes) {
            builder.addCheckpoint(new CheckpointSpec(
                    stringValue(attributes, "name"),
                    dateValue(attributes, "date")
            ));
        }
    }

    private final class SettingsBlock {
        public void grading(Closure<?> closure) {
            runClosure(closure, new GradingBlock());
        }

        public void activity(Map<?, ?> attributes) {
            java.time.LocalDate semesterStart = attributes.containsKey("semesterStart")
                    ? dateValue(attributes, "semesterStart")
                    : dateValue(attributes, "start");
            builder.activitySettings(new ActivitySettings(
                    semesterStart,
                    intValue(attributes, "totalWeeks", intValue(attributes, "weeks", 0)),
                    doubleValue(attributes, "weight", 0.0)
            ));
        }

        public void execution(Map<?, ?> attributes) {
            Object branchesValue = attributes.containsKey("preferredBranches") ? attributes.get("preferredBranches") : attributes.get("branches");
            if (branchesValue == null) {
                branchesValue = builder.executionSettings().preferredBranches();
            }
            builder.executionSettings(new ExecutionSettings(
                    java.time.Duration.ofSeconds(longValue(attributes, "timeoutSeconds", 600L)),
                    stringValue(attributes, "compileCommand", builder.executionSettings().compileCommand()),
                    stringValue(attributes, "docsCommand", builder.executionSettings().docsCommand()),
                    stringValue(attributes, "styleCommand", builder.executionSettings().styleCommand()),
                    stringValue(attributes, "testCommand", builder.executionSettings().testCommand()),
                    toStringList(branchesValue)
            ));
        }

        public void bonus(Map<?, ?> attributes) {
            builder.addBonus(new BonusSpec(
                    stringValue(attributes, "group", stringValue(attributes, "groupName", null)),
                    stringValue(attributes, "student", stringValue(attributes, "github", null)),
                    stringValue(attributes, "task", stringValue(attributes, "taskId", null)),
                    doubleValue(attributes, "points", 0.0),
                    stringValue(attributes, "reason", "")
            ));
        }
    }

    private final class GradingBlock {
        public void boundary(Map<?, ?> attributes) {
            builder.addBoundary(new GradeBoundary(
                    intValue(attributes, "minimumPercent", intValue(attributes, "min", 0)),
                    stringValue(attributes, "grade")
            ));
        }
    }

    private final class GroupBuilder {
        private final String name;
        private final java.util.ArrayList<StudentSpec> students = new java.util.ArrayList<>();

        private GroupBuilder(String name) {
            this.name = name;
        }

        public void student(Map<?, ?> attributes) {
            String name = attributes.containsKey("name") ? stringValue(attributes, "name") : stringValue(attributes, "fullName");
            String repository = attributes.containsKey("repository") ? stringValue(attributes, "repository") : stringValue(attributes, "repo");
            students.add(new StudentSpec(
                    stringValue(attributes, "github"),
                name,
                repository
            ));
        }

        private GroupSpec build() {
            return new GroupSpec(name, List.copyOf(students));
        }
    }

    private String stringValue(Map<?, ?> attributes, String key) {
        Object value = attributes.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing DSL attribute: " + key);
        }
        return value.toString();
    }

    private String stringValue(Map<?, ?> attributes, String key, String defaultValue) {
        Object value = attributes.get(key);
        return value == null ? defaultValue : value.toString();
    }

    private int intValue(Map<?, ?> attributes, String key, int defaultValue) {
        Object value = attributes.get(key);
        return value == null ? defaultValue : Integer.parseInt(value.toString());
    }

    private long longValue(Map<?, ?> attributes, String key, long defaultValue) {
        Object value = attributes.get(key);
        return value == null ? defaultValue : Long.parseLong(value.toString());
    }

    private double doubleValue(Map<?, ?> attributes, String key, double defaultValue) {
        Object value = attributes.get(key);
        return value == null ? defaultValue : Double.parseDouble(value.toString());
    }

    private java.time.LocalDate dateValue(Map<?, ?> attributes, String key) {
        Object value = attributes.get(key);
        if (value == null) {
            return null;
        }
        return java.time.LocalDate.parse(value.toString());
    }

    private java.time.LocalDate dateValue(Map<?, ?> attributes, String key, java.time.LocalDate defaultValue) {
        Object value = attributes.get(key);
        return value == null ? defaultValue : java.time.LocalDate.parse(value.toString());
    }

    private List<String> toStringList(Object value) {
        if (value == null) {
            return List.of();
        }
        if (value instanceof Collection<?> collection) {
            return collection.stream().map(Object::toString).toList();
        }
        return List.of(value.toString());
    }
}

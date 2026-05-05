package ru.nsu.gaev.oopchecker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import ru.nsu.gaev.oopchecker.engine.CheckpointGrade;
import ru.nsu.gaev.oopchecker.engine.CourseRunReport;
import ru.nsu.gaev.oopchecker.engine.GroupReport;
import ru.nsu.gaev.oopchecker.engine.StudentReport;
import ru.nsu.gaev.oopchecker.engine.TaskRunResult;
import ru.nsu.gaev.oopchecker.engine.TestCounts;
import ru.nsu.gaev.oopchecker.exec.CommandExecutor;
import ru.nsu.gaev.oopchecker.exec.CommandResult;
import ru.nsu.gaev.oopchecker.exec.SystemCommandExecutor;
import ru.nsu.gaev.oopchecker.git.GitClient;
import ru.nsu.gaev.oopchecker.git.SystemGitClient;

public final class OopCheckerEngine {
    private final CommandExecutor commandExecutor;
    private final GitClient gitClient;

    public OopCheckerEngine() {
        this(new SystemCommandExecutor(), new SystemGitClient());
    }

    public OopCheckerEngine(CommandExecutor commandExecutor, GitClient gitClient) {
        this.commandExecutor = commandExecutor;
        this.gitClient = gitClient;
    }

    public CourseRunReport run(CourseConfig config, Path workDirectory) throws IOException, InterruptedException {
        Path baseDirectory = workDirectory == null ? Path.of("").toAbsolutePath().normalize() : workDirectory.toAbsolutePath().normalize();
        Map<String, TaskSpec> taskById = config.tasksById();
        Map<String, GroupSpec> groupByName = config.groupsByName();
        Map<String, Map<String, Set<String>>> assignmentIndex = buildAssignmentIndex(config.assignments(), taskById.keySet());
        List<GroupReport> groups = new ArrayList<>();
        for (GroupSpec group : config.groups()) {
            List<StudentReport> students = new ArrayList<>();
            for (StudentSpec student : group.students()) {
                Set<String> assignedTasks = assignmentIndex
                        .getOrDefault(group.name(), Map.of())
                        .getOrDefault(student.github(), new HashSet<>(taskById.keySet()));
                if (assignedTasks.isEmpty()) {
                    assignedTasks = new HashSet<>(taskById.keySet());
                }
                students.add(runStudent(config, group, student, assignedTasks, baseDirectory, taskById));
            }
            groups.add(new GroupReport(group.name(), students));
        }
        return new CourseRunReport(config, groups);
    }

    private StudentReport runStudent(CourseConfig config, GroupSpec group, StudentSpec student, Set<String> taskIds, Path workDirectory, Map<String, TaskSpec> taskById) throws IOException, InterruptedException {
        ExecutionSettings executionSettings = config.executionSettings();
        Path repositoryRoot = gitClient.openRepository(student.repositoryUrl(), workDirectory);
        String branch = gitClient.checkoutPreferredBranch(repositoryRoot, executionSettings.preferredBranches());

        List<TaskRunResult> taskResults = new ArrayList<>();
        double rawPoints = 0.0;
        for (String taskId : taskIds) {
            TaskSpec task = taskById.get(taskId);
            if (task == null) {
                continue;
            }
            TaskRunResult taskResult = runTask(repositoryRoot, branch, student, group, task, executionSettings, config.bonuses());
            taskResults.add(taskResult);
            rawPoints += taskResult.score();
        }

        double activityPercent = calculateActivityPercent(repositoryRoot, config.activitySettings(), config.checkpoints(), taskById, taskResults);
        double finalPoints = rawPoints + rawPoints * activityPercent * config.activitySettings().weight();
        double availablePoints = taskResults.stream().mapToDouble(result -> taskById.get(result.taskId()).maxScore()).sum();
        double percent = availablePoints <= 0.0 ? 0.0 : Math.min(100.0, finalPoints / availablePoints * 100.0);
        String finalGrade = gradeFor(percent, config.gradeBoundaries());
        List<CheckpointGrade> checkpointGrades = buildCheckpointGrades(config, taskResults, taskById, repositoryRoot, activityPercent);
        return new StudentReport(group.name(), student.fullName(), student.github(), repositoryRoot.toString(), taskResults, checkpointGrades, rawPoints, activityPercent, finalPoints, finalGrade);
    }

    private TaskRunResult runTask(Path repositoryRoot, String branch, StudentSpec student, GroupSpec group, TaskSpec task, ExecutionSettings executionSettings, List<BonusSpec> bonuses) throws IOException, InterruptedException {
        CommandResult compile = commandExecutor.execute(repositoryRoot, executionSettings.compileCommand(), executionSettings.timeout());
        boolean compiled = compile.success();
        boolean docsGenerated = false;
        boolean stylePassed = false;
        TestCounts testCounts = new TestCounts(0, 0, 0);
        StringBuilder output = new StringBuilder(compile.output());
        if (compiled) {
            CommandResult docs = commandExecutor.execute(repositoryRoot, executionSettings.docsCommand(), executionSettings.timeout());
            docsGenerated = docs.success();
            appendOutput(output, docs.output());
            CommandResult style = commandExecutor.execute(repositoryRoot, executionSettings.styleCommand(), executionSettings.timeout());
            stylePassed = style.success();
            appendOutput(output, style.output());
            CommandResult tests = commandExecutor.execute(repositoryRoot, executionSettings.testCommand(), executionSettings.timeout());
            appendOutput(output, tests.output());
            testCounts = parseTestCounts(repositoryRoot, tests);
        }
        double bonusPoints = bonuses.stream()
                .filter(bonus -> matchesBonus(bonus, group.name(), student.github(), task.id()))
                .mapToDouble(BonusSpec::points)
                .sum();
        double score = scoreForTask(task, compiled, docsGenerated, stylePassed, testCounts, bonusPoints);
        return new TaskRunResult(task.id(), task.title(), compiled, docsGenerated, stylePassed, testCounts, bonusPoints, score, branch, repositoryRoot.toString(), output.toString());
    }

    private double scoreForTask(TaskSpec task, boolean compiled, boolean docsGenerated, boolean stylePassed, TestCounts testCounts, double bonusPoints) {
        if (!compiled) {
            return 0.0;
        }
        int totalTests = testCounts.passed() + testCounts.failed() + testCounts.skipped();
        double testRatio = totalTests <= 0 ? 1.0 : (double) testCounts.passed() / totalTests;
        double score = task.maxScore() * testRatio;
        if (docsGenerated) {
            score += 0.5;
        }
        if (stylePassed) {
            score += 0.5;
        }
        return Math.max(0.0, score + bonusPoints);
    }

    private TestCounts parseTestCounts(Path repositoryRoot, CommandResult tests) {
        Path junitDirectory = repositoryRoot.resolve("build/test-results/test");
        if (!Files.exists(junitDirectory)) {
            junitDirectory = repositoryRoot.resolve("target/surefire-reports");
        }
        if (Files.exists(junitDirectory)) {
            try {
                return JunitReportParser.parse(junitDirectory);
            } catch (Exception ignored) {
                // fall through to stdout parsing
            }
        }
        return parseFromOutput(tests.output());
    }

    private TestCounts parseFromOutput(String output) {
        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("passed=(\\d+)\\s+failed=(\\d+)\\s+skipped=(\\d+)", java.util.regex.Pattern.CASE_INSENSITIVE).matcher(output);
        if (matcher.find()) {
            return new TestCounts(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
        }
        return new TestCounts(0, 0, 0);
    }

    private List<CheckpointGrade> buildCheckpointGrades(CourseConfig config, List<TaskRunResult> taskResults, Map<String, TaskSpec> taskById, Path repositoryRoot, double finalActivityPercent) {
        List<CheckpointGrade> checkpointGrades = new ArrayList<>();
        for (CheckpointSpec checkpoint : config.checkpoints()) {
            double points = 0.0;
            for (TaskRunResult result : taskResults) {
                TaskSpec task = taskById.get(result.taskId());
                if (task != null && task.hardDeadline() != null && !task.hardDeadline().isAfter(checkpoint.date())) {
                    points += result.score();
                }
            }
            double activityBonus = points * finalActivityPercent * config.activitySettings().weight();
            double total = points + activityBonus;
            double max = taskResults.stream()
                    .map(task -> taskById.get(task.taskId()))
                    .filter(java.util.Objects::nonNull)
                    .filter(spec -> spec.hardDeadline() != null && !spec.hardDeadline().isAfter(checkpoint.date()))
                    .mapToDouble(TaskSpec::maxScore)
                    .sum();
            double percent = max <= 0.0 ? 0.0 : Math.min(100.0, total / max * 100.0);
            checkpointGrades.add(new CheckpointGrade(checkpoint.name(), total, gradeFor(percent, config.gradeBoundaries())));
        }
        return checkpointGrades;
    }

    private double calculateActivityPercent(Path repositoryRoot, ActivitySettings settings, List<CheckpointSpec> checkpoints, Map<String, TaskSpec> taskById, List<TaskRunResult> taskResults) throws IOException, InterruptedException {
        if (settings == null || settings.semesterStart() == null || settings.totalWeeks() <= 0) {
            return 0.0;
        }
        List<LocalDateTime> commitTimes = gitClient.commitTimes(repositoryRoot);
        if (commitTimes.isEmpty()) {
            return 0.0;
        }
        LocalDate endDate = checkpoints.isEmpty()
                ? taskResults.stream()
                .map(result -> taskById.get(result.taskId()))
                .filter(java.util.Objects::nonNull)
                .map(TaskSpec::hardDeadline)
                .filter(java.util.Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(settings.semesterStart().plusWeeks(settings.totalWeeks()))
                : checkpoints.stream().map(CheckpointSpec::date).max(Comparator.naturalOrder()).orElse(settings.semesterStart().plusWeeks(settings.totalWeeks()));
        Set<Long> activeWeeks = new HashSet<>();
        for (LocalDateTime commitTime : commitTimes) {
            LocalDate commitDate = commitTime.toLocalDate();
            if (!commitDate.isBefore(settings.semesterStart()) && !commitDate.isAfter(endDate)) {
                long week = ChronoUnit.DAYS.between(settings.semesterStart(), commitDate) / 7;
                activeWeeks.add(week);
            }
        }
        long weeks = settings.totalWeeks();
        return weeks <= 0 ? 0.0 : Math.min(1.0, activeWeeks.size() / (double) weeks);
    }

    private boolean matchesBonus(BonusSpec bonus, String groupName, String github, String taskId) {
        boolean groupMatches = bonus.groupName() == null || bonus.groupName().isBlank() || bonus.groupName().equals(groupName);
        boolean studentMatches = bonus.studentGithub() == null || bonus.studentGithub().isBlank() || bonus.studentGithub().equals(github);
        boolean taskMatches = bonus.taskId() == null || bonus.taskId().isBlank() || bonus.taskId().equals(taskId);
        return groupMatches && studentMatches && taskMatches;
    }

    private String gradeFor(double percent, List<GradeBoundary> boundaries) {
        List<GradeBoundary> sorted = new ArrayList<>(boundaries);
        sorted.sort(Comparator.comparingInt(GradeBoundary::minimumPercent).reversed());
        for (GradeBoundary boundary : sorted) {
            if (percent >= boundary.minimumPercent()) {
                return boundary.grade();
            }
        }
        return sorted.isEmpty() ? "-" : sorted.get(sorted.size() - 1).grade();
    }

    private Map<String, Map<String, Set<String>>> buildAssignmentIndex(List<AssignmentSpec> assignments, Set<String> taskIds) {
        Map<String, Map<String, Set<String>>> result = new LinkedHashMap<>();
        for (AssignmentSpec assignment : assignments) {
            result.computeIfAbsent(assignment.groupName(), ignored -> new LinkedHashMap<>())
                    .computeIfAbsent(assignment.studentGithub(), ignored -> new HashSet<>())
                    .addAll(assignment.taskIds().isEmpty() ? taskIds : assignment.taskIds());
        }
        return result;
    }

    private void appendOutput(StringBuilder builder, String output) {
        if (output != null && !output.isBlank()) {
            if (builder.length() > 0) {
                builder.append(System.lineSeparator());
            }
            builder.append(output.trim());
        }
    }

    private static final class JunitReportParser {
        private static TestCounts parse(Path directory) throws Exception {
            try (java.util.stream.Stream<Path> files = Files.list(directory)) {
                for (Path file : files.filter(path -> path.getFileName().toString().startsWith("TEST-") && path.getFileName().toString().endsWith(".xml")).toList()) {
                    org.w3c.dom.Document document = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file.toFile());
                    org.w3c.dom.Element suite = document.getDocumentElement();
                    int tests = parseInt(suite.getAttribute("tests"));
                    int failures = parseInt(suite.getAttribute("failures")) + parseInt(suite.getAttribute("errors"));
                    int skipped = parseInt(suite.getAttribute("skipped"));
                    int passed = Math.max(0, tests - failures - skipped);
                    return new TestCounts(passed, failures, skipped);
                }
            }
            return new TestCounts(0, 0, 0);
        }

        private static int parseInt(String value) {
            return value == null || value.isBlank() ? 0 : Integer.parseInt(value);
        }
    }
}

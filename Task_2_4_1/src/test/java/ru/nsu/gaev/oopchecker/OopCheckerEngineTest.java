package ru.nsu.gaev.oopchecker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.gaev.oopchecker.domain.ActivitySettings;
import ru.nsu.gaev.oopchecker.domain.AssignmentSpec;
import ru.nsu.gaev.oopchecker.domain.CheckpointSpec;
import ru.nsu.gaev.oopchecker.domain.CourseConfig;
import ru.nsu.gaev.oopchecker.domain.ExecutionSettings;
import ru.nsu.gaev.oopchecker.domain.GradeBoundary;
import ru.nsu.gaev.oopchecker.domain.GroupSpec;
import ru.nsu.gaev.oopchecker.domain.StudentSpec;
import ru.nsu.gaev.oopchecker.domain.TaskSpec;
import ru.nsu.gaev.oopchecker.engine.CourseRunReport;
import ru.nsu.gaev.oopchecker.engine.GroupReport;
import ru.nsu.gaev.oopchecker.engine.StudentReport;
import ru.nsu.gaev.oopchecker.engine.TaskRunResult;
import ru.nsu.gaev.oopchecker.exec.CommandExecutor;
import ru.nsu.gaev.oopchecker.exec.CommandResult;
import ru.nsu.gaev.oopchecker.git.GitClient;

class OopCheckerEngineTest {
    @TempDir
    Path tempDir;

    @Test
    void computesTaskScoresAndActivityForAStudent() throws Exception {
        CourseConfig config = new CourseConfig(
                List.of(new TaskSpec("2_1_1", "Простые числа", 10, LocalDate.parse("2026-03-01"), LocalDate.parse("2026-03-10"))),
                List.of(new GroupSpec("12345", List.of(new StudentSpec("nick", "Студент №1", tempDir.resolve("repo").toString())))),
                List.of(new AssignmentSpec("12345", "nick", List.of("2_1_1"))),
                List.of(new CheckpointSpec("КТ1", LocalDate.parse("2026-03-15"))),
                List.of(new GradeBoundary(90, "5"), new GradeBoundary(0, "2")),
                new ActivitySettings(LocalDate.parse("2026-02-01"), 8, 0.1),
                new ExecutionSettings(Duration.ofSeconds(1), "compile", "docs", "style", "test", List.of("main")),
                List.of()
        );

        RecordingExecutor executor = new RecordingExecutor(
                new CommandResult(0, "compile ok", false),
                new CommandResult(0, "docs ok", false),
                new CommandResult(0, "style ok", false),
                new CommandResult(0, "tests=10 passed=8 failed=1 skipped=1", false)
        );
        FakeGitClient gitClient = new FakeGitClient(tempDir.resolve("repo"));

        CourseRunReport report = new OopCheckerEngine(executor, gitClient).run(config, tempDir);

        GroupReport group = report.groups().get(0);
        StudentReport student = group.students().get(0);
        TaskRunResult task = student.taskResults().get(0);

        assertTrue(task.compiled());
        assertTrue(task.docsGenerated());
        assertTrue(task.stylePassed());
        assertEquals(8, task.testCounts().passed());
        assertEquals(1, task.testCounts().failed());
        assertEquals(1, task.testCounts().skipped());
        assertTrue(student.activityPercent() > 0.0);
        assertTrue(student.finalPoints() > 9.0);
        assertEquals("5", student.finalGrade());
        assertFalse(executor.commands.isEmpty());
        assertEquals("main", gitClient.branchName);
    }

    private static final class RecordingExecutor implements CommandExecutor {
        private final List<CommandResult> results;
        private final java.util.List<String> commands = new java.util.ArrayList<>();
        private int index;

        private RecordingExecutor(CommandResult... results) {
            this.results = List.of(results);
        }

        @Override
        public CommandResult execute(Path workingDirectory, String command, Duration timeout) throws IOException, InterruptedException {
            commands.add(command);
            if (index >= results.size()) {
                return new CommandResult(0, "", false);
            }
            return results.get(index++);
        }
    }

    private static final class FakeGitClient implements GitClient {
        private final Path repoPath;
        private String branchName;

        private FakeGitClient(Path repoPath) {
            this.repoPath = repoPath;
        }

        @Override
        public Path openRepository(String repositoryUrl, Path workDirectory) {
            return repoPath;
        }

        @Override
        public String checkoutPreferredBranch(Path repositoryRoot, List<String> preferredBranches) {
            branchName = preferredBranches.get(0);
            return branchName;
        }

        @Override
        public List<LocalDateTime> commitTimes(Path repositoryRoot) {
            return List.of(
                    LocalDateTime.parse("2026-02-03T10:00:00"),
                    LocalDateTime.parse("2026-02-10T10:00:00")
            );
        }
    }
}

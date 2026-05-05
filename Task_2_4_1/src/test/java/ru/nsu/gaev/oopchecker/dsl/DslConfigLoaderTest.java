package ru.nsu.gaev.oopchecker.dsl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.gaev.oopchecker.domain.CourseConfig;

class DslConfigLoaderTest {
    @TempDir
    Path tempDir;

    @Test
    void loadsAndMergesIncludedDslFiles() throws Exception {
        Path base = tempDir.resolve("base.groovy");
        Files.writeString(base, """
                tasks {
                    task id: '2_1_1', title: 'Простые числа', maxScore: 10, softDeadline: '2026-03-01', hardDeadline: '2026-03-10'
                }
                settings {
                    execution compileCommand: 'compile-base', docsCommand: 'docs-base', styleCommand: 'style-base', testCommand: 'test-base', preferredBranches: ['main']
                }
                """);

        Path script = tempDir.resolve("oop-checker.groovy");
        Files.writeString(script, """
                include 'base.groovy'
                groups {
                    group(name: '12345') {
                        student(github: 'nick', name: 'Студент №1', repository: 'repo-1')
                    }
                }
                checks {
                    check(group: '12345', student: 'nick', tasks: ['2_1_1'])
                }
                checkpoints {
                    checkpoint(name: 'КТ1', date: '2026-03-15')
                }
                settings {
                    activity semesterStart: '2026-02-01', totalWeeks: 12, weight: 0.2
                }
                """);

        CourseConfig config = new DslConfigLoader().load(script);

        assertEquals(1, config.tasks().size());
        assertEquals(1, config.groups().size());
        assertEquals(1, config.assignments().size());
        assertEquals(1, config.checkpoints().size());
        assertEquals("compile-base", config.executionSettings().compileCommand());
        assertEquals(1, config.executionSettings().preferredBranches().size());
        assertEquals("main", config.executionSettings().preferredBranches().get(0));
        assertTrue(config.activitySettings().weight() > 0.0);
    }
}

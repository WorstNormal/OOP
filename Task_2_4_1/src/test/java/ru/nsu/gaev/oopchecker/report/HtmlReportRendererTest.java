package ru.nsu.gaev.oopchecker.report;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.oopchecker.domain.ActivitySettings;
import ru.nsu.gaev.oopchecker.domain.AssignmentSpec;
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

class HtmlReportRendererTest {
    @Test
    void rendersReadableHtmlReport() {
        CourseConfig config = new CourseConfig(
                List.of(new TaskSpec("2_1_1", "Простые числа", 10, null, null)),
                List.of(new GroupSpec("12345", List.of(new StudentSpec("nick", "Студент №1", "repo")))),
                List.of(new AssignmentSpec("12345", "nick", List.of("2_1_1"))),
                List.of(new CheckpointSpec("КТ1", java.time.LocalDate.parse("2026-03-15"))),
                List.of(new GradeBoundary(90, "5"), new GradeBoundary(0, "2")),
                new ActivitySettings(java.time.LocalDate.parse("2026-02-01"), 12, 0.2),
                new ExecutionSettings(java.time.Duration.ofMinutes(1), "compile", "docs", "style", "test", List.of("main")),
                List.of()
        );
        CourseRunReport report = new CourseRunReport(
                config,
                List.of(new GroupReport(
                        "12345",
                        List.of(new StudentReport(
                                "12345",
                                "Студент №1",
                                "nick",
                                "repo",
                                List.of(new TaskRunResult("2_1_1", "Простые числа", true, true, true, new TestCounts(10, 0, 0), 0.0, 10.0, "main", "repo", "ok")),
                                List.of(new CheckpointGrade("КТ1", 10.0, "5")),
                                10.0,
                                0.5,
                                11.0,
                                "5"
                        ))
                ))
        );

        String html = new HtmlReportRenderer().render(report);

        assertTrue(html.contains("Группа 12345"));
        assertTrue(html.contains("Студент №1"));
        assertTrue(html.contains("10/0/0"));
        assertTrue(html.contains("11"));
        assertTrue(html.contains("5"));
    }
}

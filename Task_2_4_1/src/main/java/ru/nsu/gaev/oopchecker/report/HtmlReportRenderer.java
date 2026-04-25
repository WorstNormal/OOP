package ru.nsu.gaev.oopchecker.report;

import java.util.List;

import ru.nsu.gaev.oopchecker.engine.CheckpointGrade;
import ru.nsu.gaev.oopchecker.engine.CourseRunReport;
import ru.nsu.gaev.oopchecker.engine.GroupReport;
import ru.nsu.gaev.oopchecker.engine.StudentReport;
import ru.nsu.gaev.oopchecker.engine.TaskRunResult;
import ru.nsu.gaev.oopchecker.engine.TestCounts;

public final class HtmlReportRenderer {
    public String render(CourseRunReport report) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><meta charset=\"UTF-8\"><title>oop-checker</title>");
        html.append("<style>body{font-family:Arial,sans-serif;margin:20px}table{border-collapse:collapse;width:100%;margin-bottom:24px}th,td{border:1px solid #ccc;padding:6px 8px;vertical-align:top}th{background:#f4f4f4}.ok{color:#0a0}.bad{color:#b00}</style>");
        html.append("</head><body>");
        for (GroupReport group : report.groups()) {
            html.append("<h2>Группа ").append(escape(group.groupName())).append("</h2>");
            for (StudentReport student : group.students()) {
                appendStudent(report, html, student);
            }
            appendSummary(html, group.students());
        }
        html.append("</body></html>");
        return html.toString();
    }

    private void appendStudent(CourseRunReport report, StringBuilder html, StudentReport student) {
        html.append("<h3>").append(escape(student.studentName())).append("</h3>");
        html.append("<div>GitHub: ").append(escape(student.github())).append("</div>");
        html.append("<div>Repository: ").append(escape(student.repositoryPath())).append("</div>");
        html.append("<table><thead><tr><th>Лабораторная</th><th>Сборка</th><th>Документация</th><th>Style guide</th><th>Тесты</th><th>Доп. балл</th><th>Балл</th></tr></thead><tbody>");
        for (TaskRunResult result : student.taskResults()) {
            html.append("<tr>");
            html.append(cell(result.taskTitle()));
            html.append(flagCell(result.compiled()));
            html.append(flagCell(result.docsGenerated()));
            html.append(flagCell(result.stylePassed()));
            html.append(testCell(result.testCounts()));
            html.append(cell(formatNumber(result.bonusPoints())));
            html.append(cell(formatNumber(result.score())));
            html.append("</tr>");
        }
        html.append("</tbody></table>");
        html.append("<div>Сумма: ").append(formatNumber(student.rawPoints())).append(", активность: ").append(formatPercent(student.activityPercent())).append(", итог: ").append(formatNumber(student.finalPoints())).append(", оценка: ").append(escape(student.finalGrade())).append("</div>");
        if (!student.checkpointGrades().isEmpty()) {
            html.append("<table><thead><tr><th>Контрольная точка</th><th>Балл</th><th>Оценка</th></tr></thead><tbody>");
            for (CheckpointGrade checkpointGrade : student.checkpointGrades()) {
                html.append("<tr>").append(cell(checkpointGrade.checkpointName())).append(cell(formatNumber(checkpointGrade.points()))).append(cell(checkpointGrade.grade())).append("</tr>");
            }
            html.append("</tbody></table>");
        }
    }

    private void appendSummary(StringBuilder html, List<StudentReport> students) {
        html.append("<h3>Общая статистика группы</h3>");
        html.append("<table><thead><tr><th>Студент</th><th>Сумма</th><th>Активность</th><th>Оценка</th></tr></thead><tbody>");
        for (StudentReport student : students) {
            html.append("<tr><td>").append(escape(student.studentName())).append("</td><td>").append(formatNumber(student.finalPoints())).append("</td><td>").append(formatPercent(student.activityPercent())).append("</td><td>").append(escape(student.finalGrade())).append("</td></tr>");
        }
        html.append("</tbody></table>");
    }

    private String cell(String value) {
        return "<td>" + escape(value) + "</td>";
    }

    private String flagCell(boolean value) {
        return value ? "<td class=\"ok\">+</td>" : "<td class=\"bad\">-</td>";
    }

    private String testCell(TestCounts counts) {
        return "<td>" + counts.passed() + "/" + counts.failed() + "/" + counts.skipped() + "</td>";
    }

    private String formatNumber(double value) {
        if (Math.abs(value - Math.rint(value)) < 0.00001) {
            return Long.toString(Math.round(value));
        }
        return String.format(java.util.Locale.ROOT, "%.2f", value);
    }

    private String formatPercent(double value) {
        return String.format(java.util.Locale.ROOT, "%.0f%%", value * 100.0);
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}

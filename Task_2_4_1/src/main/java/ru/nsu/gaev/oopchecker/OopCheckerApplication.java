package ru.nsu.gaev.oopchecker;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import ru.nsu.gaev.oopchecker.engine.CourseRunReport;
import ru.nsu.gaev.oopchecker.dsl.DslConfigLoader;
import ru.nsu.gaev.oopchecker.report.HtmlReportRenderer;

public final class OopCheckerApplication {
    public void run(String[] args, PrintStream out) throws Exception {
        Path scriptPath = resolveScriptPath(args);
        CourseRunReport report = new OopCheckerEngine().run(new DslConfigLoader().load(scriptPath), scriptPath.getParent());
        out.println(new HtmlReportRenderer().render(report));
    }

    private Path resolveScriptPath(String[] args) {
        Path workingDirectory = Paths.get("").toAbsolutePath().normalize();
        if (args != null && args.length > 1) {
            Path explicit = workingDirectory.resolve(args[1]).normalize();
            if (Files.exists(explicit)) {
                return explicit;
            }
        }
        if (args != null && args.length > 0) {
            Path explicit = workingDirectory.resolve(args[0]).normalize();
            if (Files.exists(explicit) && explicit.toString().endsWith(".groovy")) {
                return explicit;
            }
        }
        Path defaultScript = workingDirectory.resolve("oop-checker.groovy");
        if (Files.exists(defaultScript)) {
            return defaultScript;
        }
        throw new IllegalStateException("Cannot find oop-checker.groovy in working directory");
    }
}

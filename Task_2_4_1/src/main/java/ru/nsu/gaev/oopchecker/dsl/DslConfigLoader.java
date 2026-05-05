package ru.nsu.gaev.oopchecker.dsl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import ru.nsu.gaev.oopchecker.domain.CourseConfig;

public final class DslConfigLoader {
    private final ThreadLocal<Deque<Path>> loadingStack = ThreadLocal.withInitial(ArrayDeque::new);
    private final ThreadLocal<Set<Path>> loadingSet = ThreadLocal.withInitial(HashSet::new);

    public CourseConfig load(Path scriptPath) throws IOException {
        Path normalized = scriptPath.toAbsolutePath().normalize();
        if (!Files.exists(normalized)) {
            throw new IllegalArgumentException("DSL script not found: " + normalized);
        }
        Deque<Path> stack = loadingStack.get();
        Set<Path> set = loadingSet.get();
        if (!set.add(normalized)) {
            throw new IllegalStateException("Cyclic DSL import detected: " + normalized);
        }
        stack.push(normalized);
        try {
            CompilerConfiguration configuration = new CompilerConfiguration();
            configuration.setScriptBaseClass(CourseDslScript.class.getName());
            GroovyShell shell = new GroovyShell(getClass().getClassLoader(), new Binding(), configuration);
            CourseDslScript script = (CourseDslScript) shell.parse(Files.readString(normalized, StandardCharsets.UTF_8), normalized.getFileName().toString());
            script.setLoader(this);
            script.setSourceFile(normalized);
            script.run();
            return script.getConfig();
        } finally {
            stack.pop();
            set.remove(normalized);
        }
    }

    CourseConfig loadImported(Path sourceFile, String relativePath) throws IOException {
        Path imported = sourceFile.getParent() == null ? Path.of(relativePath) : sourceFile.getParent().resolve(relativePath);
        return load(imported);
    }
}

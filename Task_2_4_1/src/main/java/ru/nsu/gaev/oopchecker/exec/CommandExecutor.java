package ru.nsu.gaev.oopchecker.exec;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

public interface CommandExecutor {
    CommandResult execute(Path workingDirectory, String command, Duration timeout) throws IOException, InterruptedException;
}

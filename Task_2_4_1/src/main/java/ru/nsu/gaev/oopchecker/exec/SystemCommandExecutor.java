package ru.nsu.gaev.oopchecker.exec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class SystemCommandExecutor implements CommandExecutor {
    @Override
    public CommandResult execute(Path workingDirectory, String command, Duration timeout) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("cmd", "/c", command)
                .directory(workingDirectory.toFile())
                .redirectErrorStream(true)
                .start();
        boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);
        if (!finished) {
            process.destroyForcibly();
            return new CommandResult(-1, readAll(process.getInputStream()), true);
        }
        return new CommandResult(process.exitValue(), readAll(process.getInputStream()), false);
    }

    private String readAll(InputStream inputStream) throws IOException {
        try (InputStream in = inputStream; ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            in.transferTo(output);
            return output.toString(StandardCharsets.UTF_8);
        }
    }
}

package ru.nsu.gaev.oopchecker.git;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import ru.nsu.gaev.oopchecker.exec.CommandExecutor;
import ru.nsu.gaev.oopchecker.exec.CommandResult;
import ru.nsu.gaev.oopchecker.exec.SystemCommandExecutor;

public final class SystemGitClient implements GitClient {
    private final CommandExecutor commandExecutor;

    public SystemGitClient() {
        this(new SystemCommandExecutor());
    }

    public SystemGitClient(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @Override
    public Path openRepository(String repositoryUrl, Path workDirectory) throws IOException, InterruptedException {
        Path localPath = toLocalPath(repositoryUrl);
        if (localPath != null && Files.exists(localPath)) {
            return localPath.toAbsolutePath().normalize();
        }
        Path targetDirectory = Files.createTempDirectory(workDirectory, "oop-checker-repo-");
        CommandResult result = commandExecutor.execute(workDirectory, "git clone --quiet " + quote(repositoryUrl) + " " + quote(targetDirectory.toString()), java.time.Duration.ofMinutes(5));
        if (!result.success()) {
            throw new IOException("Failed to clone repository: " + result.output());
        }
        return targetDirectory;
    }

    @Override
    public String checkoutPreferredBranch(Path repositoryRoot, List<String> preferredBranches) throws IOException, InterruptedException {
        for (String branch : preferredBranches) {
            CommandResult probe = commandExecutor.execute(repositoryRoot, "git rev-parse --verify --quiet " + quote(branch), java.time.Duration.ofSeconds(30));
            if (probe.success()) {
                CommandResult checkout = commandExecutor.execute(repositoryRoot, "git checkout --quiet " + quote(branch), java.time.Duration.ofMinutes(1));
                if (!checkout.success()) {
                    throw new IOException("Cannot checkout branch " + branch + ": " + checkout.output());
                }
                return branch;
            }
        }
        return preferredBranches.isEmpty() ? "HEAD" : preferredBranches.get(0);
    }

    @Override
    public List<LocalDateTime> commitTimes(Path repositoryRoot) throws IOException, InterruptedException {
        CommandResult result = commandExecutor.execute(repositoryRoot, "git log --format=%ct", java.time.Duration.ofMinutes(1));
        if (!result.success() && result.output().isBlank()) {
            return List.of();
        }
        List<LocalDateTime> commitTimes = new ArrayList<>();
        for (String line : result.output().split("\\R")) {
            if (!line.isBlank()) {
                commitTimes.add(LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(line.trim())), ZoneOffset.UTC));
            }
        }
        return commitTimes;
    }

    private Path toLocalPath(String repositoryUrl) {
        try {
            if (repositoryUrl.startsWith("file:")) {
                return Path.of(java.net.URI.create(repositoryUrl));
            }
            Path direct = Path.of(repositoryUrl);
            if (Files.exists(direct)) {
                return direct;
            }
            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    private String quote(String value) {
        return '"' + value.replace("\"", "\\\"") + '"';
    }
}

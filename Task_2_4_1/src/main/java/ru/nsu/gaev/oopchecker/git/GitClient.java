package ru.nsu.gaev.oopchecker.git;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

public interface GitClient {
    Path openRepository(String repositoryUrl, Path workDirectory) throws IOException, InterruptedException;

    String checkoutPreferredBranch(Path repositoryRoot, List<String> preferredBranches) throws IOException, InterruptedException;

    List<LocalDateTime> commitTimes(Path repositoryRoot) throws IOException, InterruptedException;
}

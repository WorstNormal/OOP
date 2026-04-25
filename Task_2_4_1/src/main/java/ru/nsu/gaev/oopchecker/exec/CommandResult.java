package ru.nsu.gaev.oopchecker.exec;

public record CommandResult(
        int exitCode,
        String output,
        boolean timedOut
) {
    public boolean success() {
        return exitCode == 0 && !timedOut;
    }
}

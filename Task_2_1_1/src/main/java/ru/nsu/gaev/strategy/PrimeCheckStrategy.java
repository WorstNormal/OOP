package ru.nsu.gaev.strategy;

/**
 * Interface for strategies checking for composite numbers in an array.
 */
public interface PrimeCheckStrategy {
    /**
     * Checks if the array contains any composite number.
     *
     * @param array the array of numbers to check
     * @return true if at least one composite number is found, false otherwise
     */
    boolean hasComposite(int[] array);
}

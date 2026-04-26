package ru.nsu.gaev.strategy;

import java.util.Arrays;
import ru.nsu.gaev.PrimeUtils;

/**
 * Parallel execution strategy using parallelStream().
 */
public class ParallelStreamStrategy implements PrimeCheckStrategy {
    @Override
    public boolean hasComposite(int[] array) {
        return Arrays.stream(array)
                .parallel()
                .anyMatch(num -> !PrimeUtils.isPrime(num));
    }
}

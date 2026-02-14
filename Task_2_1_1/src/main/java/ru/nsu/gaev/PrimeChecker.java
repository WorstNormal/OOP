package ru.nsu.gaev;

import ru.nsu.gaev.strategy.ParallelStreamStrategy;
import ru.nsu.gaev.strategy.ParallelThreadsStrategy;
import ru.nsu.gaev.strategy.PrimeCheckStrategy;
import ru.nsu.gaev.strategy.SequentialStrategy;

/**
 * Checker for composite numbers in an array using various parallelization strategies.
 */
public class PrimeChecker {
  private final int[] array;

  /**
   * Constructs a PrimeChecker with the given array.
   *
   * @param array the array to check
   */
  public PrimeChecker(int[] array) {
    this.array = array;
  }

  /**
   * Sequential execution: checks for composite numbers.
   *
   * @return true if composite number found, false otherwise
   */
  public boolean hasCompositeSequential() {
    final PrimeCheckStrategy strategy = new SequentialStrategy();
    return strategy.hasComposite(array);
  }

  /**
   * Parallel execution using threads.
   *
   * @param threadCount number of threads to use
   * @return true if composite number found, false otherwise
   */
  public boolean hasCompositeParallel(int threadCount) {
    final ParallelThreadsStrategy strategy = new ParallelThreadsStrategy(threadCount);
    return strategy.hasComposite(array);
  }

  /**
   * Parallel execution using parallel stream.
   *
   * @return true if composite number found, false otherwise
   */
  public boolean hasCompositeParallelStream() {
    final PrimeCheckStrategy strategy = new ParallelStreamStrategy();
    return strategy.hasComposite(array);
  }
}

package ru.nsu.gaev;

/**
 * Checker for composite numbers in an array using various parallelization.
 * Provides sequential, parallel Thread-based, and parallel stream approaches.
 */
public class PrimeChecker {
  private final int[] array;

  private volatile boolean foundComposite;

  /**
   * Constructs a PrimeChecker with the given array.
   *
   * @param array the array to check
   */
  public PrimeChecker(int[] array) {
    this.array = array;
    this.foundComposite = false;
  }

  /**
   * Checks if a number is prime.
   *
   * @param num the number to check
   * @return true if prime, false otherwise
   */
  private static boolean isPrime(int num) {
    if (num < 2) {
      return false;
    }
    if (num == 2) {
      return true;
    }
    if (num % 2 == 0) {
      return false;
    }
    // Check divisibility by odd numbers up to sqrt(num)
    for (int i = 3; (long) i * i <= num; i += 2) {
      if (num % i == 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Sequential execution: checks for composite numbers.
   *
   * @return true if composite number found, false otherwise
   */
  public boolean hasCompositeSequential() {
    for (int num : array) {
      if (!isPrime(num)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Parallel execution using threads.
   *
   * @param threadCount number of threads to use
   * @return true if composite number found, false otherwise
   */
  public boolean hasCompositeParallel(int threadCount) {
    foundComposite = false;

    // Protection from invalid input data
    if (array == null || array.length == 0) {
      return false;
    }
    if (threadCount <= 0) {
      threadCount = 1;
    }

    // If threads > elements, reduce thread count
    if (array.length < threadCount) {
      threadCount = array.length;
    }

    // Create and start threads
    final Thread[] threads = new Thread[threadCount];
    final int elementsPerThread = array.length / threadCount;

    for (int t = 0; t < threadCount; t++) {
      final int startIdx = t * elementsPerThread;
      final int endIdx = (t == threadCount - 1) ? array.length
          : (t + 1) * elementsPerThread;

      threads[t] = new Thread(() -> {
        // Each thread checks its range
        for (int i = startIdx; i < endIdx; i++) {
          if (!isPrime(array[i])) {
            foundComposite = true;
            break;
          }
        }
      });

      threads[t].start();
    }

    // Wait for all threads to finish
    for (Thread thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return false;
      }
    }

    return foundComposite;
  }

  /**
   * Parallel execution using parallel stream.
   *
   * @return true if composite number found, false otherwise
   */
  public boolean hasCompositeParallelStream() {
    return java.util.Arrays.stream(array)
        .parallel()
        .anyMatch(num -> !isPrime(num));
  }
}

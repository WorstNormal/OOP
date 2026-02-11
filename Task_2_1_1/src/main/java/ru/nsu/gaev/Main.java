package ru.nsu.gaev;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Main class for testing PrimeChecker performance.
 * Demonstrates sequential, parallel (threads), and parallel stream approaches.
 */
public class Main {
  /**
   * Main entry point for the application.
   *
   * @param args command line arguments (not used)
   */
  public static void main(String[] args) {
    // Test set 1: mixed numbers
    final int[] test1 = {6, 8, 7, 13, 5, 9, 4};

    // Test set 2: large prime numbers
    final int[] test2 = {20319251, 6997901, 6997927, 6997937, 17858849,
      6997967, 6998009, 6998029, 6998039, 20165149, 6998051, 6998053};

    // Test set 3: extended set of large prime numbers
    final int[] test3 = generateLargePrimes();

    System.out.println("=== Test 1: [6, 8, 7, 13, 5, 9, 4] ===");
    testAllMethods(test1, "test1_results.csv");

    System.out.println("\n=== Test 2: Large prime numbers ===");
    testAllMethods(test2, "test2_results.csv");

    System.out.println("\n=== Test 3: Extended large primes ("
        + test3.length + " numbers) ===");
    testAllMethods(test3, "test3_results.csv");

    System.out.println("\nResults saved to CSV files.");
  }

  /**
   * Tests all methods with given array and saves results to CSV.
   *
   * @param arr input array to test
   * @param csvFile output file for results
   */
  private static void testAllMethods(int[] arr, String csvFile) {
    final PrimeChecker checker = new PrimeChecker(arr);

    final StringBuilder csv = new StringBuilder();
    csv.append("Method,NumThreads,TimeMs\n");

    // 1. Sequential execution
    System.out.println("1. Sequential execution:");
    final double[] seqTimes = new double[5];
    for (int run = 0; run < 5; run++) {
      final long startTime = System.nanoTime();
      final boolean result1 = checker.hasCompositeSequential();
      final long time1 = System.nanoTime() - startTime;
      final double timeMs = time1 / 1_000_000.0;
      seqTimes[run] = timeMs;
      System.out.printf("   Run %d: %b (%.2f ms)%n", run + 1, result1,
          timeMs);
      csv.append(String.format("Sequential,1,%.2f%n", timeMs));
    }
    final double avgSeq = average(seqTimes);
    System.out.printf("   Average: %.2f ms%n", avgSeq);

    // 2. Parallel with Threads (different number of threads)
    System.out.println("2. Parallel with Threads:");
    for (int threads = 1; threads <= 8; threads *= 2) {
      System.out.printf("   Threads: %d%n", threads);
      final double[] threadTimes = new double[5];
      for (int run = 0; run < 5; run++) {
        final long startTime = System.nanoTime();
        final boolean result2 = checker.hasCompositeParallel(threads);
        final long time2 = System.nanoTime() - startTime;
        final double timeMs = time2 / 1_000_000.0;
        threadTimes[run] = timeMs;
        System.out.printf("      Run %d: %b (%.2f ms)%n", run + 1, result2,
            timeMs);
        csv.append(String.format("Parallel,%d,%.2f%n", threads, timeMs));
      }
      final double avgThread = average(threadTimes);
      System.out.printf("      Average: %.2f ms%n", avgThread);
    }

    // 3. ParallelStream
    System.out.println("3. ParallelStream:");
    final double[] streamTimes = new double[5];
    for (int run = 0; run < 5; run++) {
      final long startTime = System.nanoTime();
      final boolean result3 = checker.hasCompositeParallelStream();
      final long time3 = System.nanoTime() - startTime;
      final double timeMs = time3 / 1_000_000.0;
      streamTimes[run] = timeMs;
      System.out.printf("   Run %d: %b (%.2f ms)%n", run + 1, result3,
          timeMs);
      csv.append(String.format("ParallelStream,AUTO,%.2f%n", timeMs));
    }
    final double avgStream = average(streamTimes);
    System.out.printf("   Average: %.2f ms%n", avgStream);

    // Save results to CSV in directory from System.getProperty("user.dir")
    final String userDir = System.getProperty("user.dir");
    final File outFile = new File(userDir, csvFile);
    try (PrintWriter writer = new PrintWriter(outFile)) {
      writer.print(csv.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Calculates average of array values.
   *
   * @param values input array
   * @return average value
   */
  public static double average(double[] values) {
    double sum = 0;
    for (double v : values) {
      sum += v;
    }
    return sum / values.length;
  }

  /**
   * Generates array of large prime numbers.
   *
   * @return array of prime numbers
   */
  public static int[] generateLargePrimes() {
    final int[] largePrimes = {
      2147483647, 2147483629, 2147483587, 2147483579, 2147483563,
      2147483549, 2147483543, 2147483539, 2147483537, 2147483527,
      2147483521, 2147483507, 2147483501, 2147483497, 2147483489,
      2147483477, 2147483471, 2147483461, 2147483459, 2147483449,
      2147483443, 2100000037, 2099999999, 2099999927, 2099999891,
      2099999837, 2000000011, 1999999937, 1999999829, 1999999827,
      1999999823, 1900000007, 1899999937, 1899999831, 1899999829,
      1899999823, 1800000023, 1799999999, 1799999927, 1799999891,
      1799999837, 1700000003, 1699999999, 1699999927, 1699999891,
      1699999837, 1600000007, 1599999937, 1599999829, 1599999827,
      1599999823
    };
    return largePrimes;
  }
}
package ru.nsu.gaev;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

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

        // 1. Sequential execution
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
     * @param arr     input array to test
     * @param csvFile output file for results
     */
    private static void testAllMethods(int[] arr, String csvFile) {
        final PrimeChecker checker = new PrimeChecker(arr);

        final StringBuilder csv = new StringBuilder();
        csv.append("Method,NumThreads,TimeMs\n");

        // 1. Sequential execution
        System.out.println("1. Sequential execution:");
        final double[] seqTimes = new double[5];
        // Warmup
        checker.hasCompositeSequential();

        for (int run = 0; run < 5; run++) {
            final long startTime = System.nanoTime();
            final boolean result1 = checker.hasCompositeSequential();
            final long time1 = System.nanoTime() - startTime;
            final double timeMs = time1 / 1_000_000.0;
            seqTimes[run] = timeMs;
            // System.out.printf("   Run %d: %b (%.2f ms)%n", run + 1, result1, timeMs);
        }
        final double avgSeq = average(seqTimes);
        System.out.printf("   Average: %.2f ms%n", avgSeq);
        csv.append(String.format(Locale.US, "Sequential,1,%.2f%n", avgSeq));

        // 2. Parallel with Threads (different number of threads)
        System.out.println("2. Parallel with Threads:");
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("   Available processors: " + availableProcessors);

        // Test for 1 to availableProcessors * 2 threads
        for (int threads = 1; threads <= availableProcessors * 2; threads++) {
            // Only test specific thread counts to keep graph readable but cover the range
            // e.g. 1, 2, 3, 4, ... up to max, maybe skipping if too many
            if (threads > 16 && threads % 4 != 0) {
                continue;
            }

            final double[] threadTimes = new double[5];
            // Warmup
            checker.hasCompositeParallel(threads);

            for (int run = 0; run < 5; run++) {
                final long startTime = System.nanoTime();
                final boolean result2 = checker.hasCompositeParallel(threads);
                final long time2 = System.nanoTime() - startTime;
                final double timeMs = time2 / 1_000_000.0;
                threadTimes[run] = timeMs;
            }
            final double avgThread = average(threadTimes);
            System.out.printf("   Threads %d Average: %.2f ms%n", threads, avgThread);
            csv.append(String.format(Locale.US, "Parallel,%d,%.2f%n", threads, avgThread));
        }

        // 3. ParallelStream
        System.out.println("3. ParallelStream:");
        final double[] streamTimes = new double[5];
        // Warmup
        checker.hasCompositeParallelStream();

        for (int run = 0; run < 5; run++) {
            final long startTime = System.nanoTime();
            final boolean result3 = checker.hasCompositeParallelStream();
            final long time3 = System.nanoTime() - startTime;
            final double timeMs = time3 / 1_000_000.0;
            streamTimes[run] = timeMs;
            // System.out.printf("   Run %d: %b (%.2f ms)%n", run + 1, result3, timeMs);
        }
        final double avgStream = average(streamTimes);
        System.out.printf("   Average: %.2f ms%n", avgStream);
        csv.append(String.format(Locale.US, "ParallelStream,%d,%.2f%n",
                availableProcessors, avgStream));

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
        // Repeat the set of large primes to increase load
        final int[] sourcePrimes = {
                2147483647, 2147483629, 2147483587, 2147483579, 2147483563,
                2147483549, 2147483543, 2147483497, 2147483489, 2147483477,
                2147483423, 2147483399, 2147483353, 2147483323, 2147483269,
                2147483249, 2147483237, 2147483179, 2147483171, 2147483137,
                2147483123, 2147483077, 2147483069, 2147483059, 2147483053,
                2147483033, 2147483029, 2147482951, 2147482949, 2147482943,
                2147482937, 2147482921, 2147482877, 2147482873, 2147482867,
                2147482859, 2147482819, 2147482817, 2147482811, 2147482801,
                2147482763, 2147482739, 2147482697, 2147482693, 2147482681,
                2147482663, 2147482661, 2147482621, 2147482591, 2147482583
        };

        // Create a larger array by repeating the source primes
        // Increase multiplier to ensure heavy load
        final int multiplier = 200;
        final int[] largePrimes = new int[sourcePrimes.length * multiplier];
        for (int i = 0; i < multiplier; i++) {
            System.arraycopy(sourcePrimes, 0, largePrimes, i * sourcePrimes.length,
                    sourcePrimes.length);
        }
        return largePrimes;
    }
}
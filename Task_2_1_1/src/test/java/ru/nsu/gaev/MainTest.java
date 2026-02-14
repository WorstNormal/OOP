package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Integration tests for Main class.
 */
public class MainTest {

  @Test
  public void testMainExecution(@TempDir Path tempDir) {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    final PrintStream originalOut = System.out;

    try {
      System.setOut(new PrintStream(outContent));

      // Change working directory to temp dir for CSV output
      final String originalDir = System.getProperty("user.dir");
      System.setProperty("user.dir", tempDir.toString());

      // Run main method
      assertDoesNotThrow(() -> Main.main(new String[]{}));

      // Restore output
      System.setOut(originalOut);
      System.setProperty("user.dir", originalDir);

      final String output = outContent.toString();
      assertTrue(output.contains("Test 1"),
          "Output should contain Test 1");
      assertTrue(output.contains("Test 2"),
          "Output should contain Test 2");
      assertTrue(output.contains("Test 3"),
          "Output should contain Test 3");
      assertTrue(output.contains("Sequential execution"),
          "Output should contain Sequential execution");
      assertTrue(output.contains("Parallel with Threads"),
          "Output should contain Parallel with Threads");
      assertTrue(output.contains("ParallelStream"),
          "Output should contain ParallelStream");
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testMainWithCSVFileGeneration(@TempDir Path tempDir)
      throws IOException {
    final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    final PrintStream originalOut = System.out;

    try {
      System.setOut(new PrintStream(outContent));
      final String originalDir = System.getProperty("user.dir");
      System.setProperty("user.dir", tempDir.toString());

      assertDoesNotThrow(() -> Main.main(new String[]{}));

      System.setOut(originalOut);
      System.setProperty("user.dir", originalDir);

      // Check if CSV files were created
      final File test1Csv = tempDir.resolve("test1_results.csv").toFile();
      final File test2Csv = tempDir.resolve("test2_results.csv").toFile();
      final File test3Csv = tempDir.resolve("test3_results.csv").toFile();

      assertTrue(test1Csv.exists(), "test1_results.csv should exist");
      assertTrue(test2Csv.exists(), "test2_results.csv should exist");
      assertTrue(test3Csv.exists(), "test3_results.csv should exist");

      // Verify CSV content
      final String test1Content = Files.readString(test1Csv.toPath());
      assertTrue(test1Content.contains("Method,NumThreads,TimeMs"),
          "CSV should have header");
      assertTrue(test1Content.contains("Sequential"),
          "CSV should contain Sequential data");
      assertTrue(test1Content.contains("Parallel"),
          "CSV should contain Parallel data");
      assertTrue(test1Content.contains("ParallelStream"),
          "CSV should contain ParallelStream data");
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  public void testAverageCalculation() {
    final double[] values = {10.0, 20.0, 30.0, 40.0, 50.0};
    final double avg = Main.average(values);
    assertTrue(Math.abs(avg - 30.0) < 0.01,
        "Average should be 30.0");
  }

  @Test
  public void testAverageSingleValue() {
    final double[] values = {5.0};
    final double avg = Main.average(values);
    assertTrue(Math.abs(avg - 5.0) < 0.01,
        "Average should be 5.0");
  }

  @Test
  public void testGenerateLargePrimes() {
    final int[] primes = Main.generateLargePrimes();
    assertTrue(primes.length > 0,
        "generateLargePrimes should return non-empty array");
    assertTrue(primes.length >= 50,
        "generateLargePrimes should return at least 50 primes");
  }
}

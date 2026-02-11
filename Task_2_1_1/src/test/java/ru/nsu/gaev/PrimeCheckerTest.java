package ru.nsu.gaev;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for PrimeChecker.
 */
public class PrimeCheckerTest {

  @Test
  public void testSequentialAllPrimes() {
    final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19};
    final PrimeChecker checker = new PrimeChecker(primes);
    assertFalse(checker.hasCompositeSequential(),
        "All primes -> no composite expected");
  }

  @Test
  public void testSequentialContainsComposite() {
    final int[] arr = {4, 5, 7, 11};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertTrue(checker.hasCompositeSequential(),
        "Contains 4 -> composite expected");
  }

  @Test
  public void testSequentialSingleElement() {
    final int[] arr1 = {2};
    final PrimeChecker checker1 = new PrimeChecker(arr1);
    assertFalse(checker1.hasCompositeSequential());

    final int[] arr2 = {4};
    final PrimeChecker checker2 = new PrimeChecker(arr2);
    assertTrue(checker2.hasCompositeSequential());
  }

  @Test
  public void testSequentialSmallNumbers() {
    final int[] arr = {0, 1, 2};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertTrue(checker.hasCompositeSequential());
  }

  @Test
  public void testParallelAllPrimesVariousThreads() {
    final int[] primes = {2, 3, 5, 7, 11, 13, 17, 19};
    final PrimeChecker checker = new PrimeChecker(primes);
    assertFalse(checker.hasCompositeParallel(1));
    assertFalse(checker.hasCompositeParallel(2));
    assertFalse(checker.hasCompositeParallel(4));
  }

  @Test
  public void testParallelContainsCompositeVariousPositions() {
    final int[] arr1 = {4, 3, 5, 7, 11};
    final int[] arr2 = {2, 3, 5, 9, 11};
    final int[] arr3 = {2, 3, 5, 7, 15};

    final PrimeChecker c1 = new PrimeChecker(arr1);
    final PrimeChecker c2 = new PrimeChecker(arr2);
    final PrimeChecker c3 = new PrimeChecker(arr3);

    assertTrue(c1.hasCompositeParallel(2));
    assertTrue(c2.hasCompositeParallel(3));
    assertTrue(c3.hasCompositeParallel(4));
  }

  @Test
  public void testParallelNegativeOrLargeThreadCount() {
    final int[] arr = {2, 3, 5, 7, 11};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertFalse(checker.hasCompositeParallel(-1));
    assertFalse(checker.hasCompositeParallel(100));
  }

  @Test
  public void testParallelSingleThread() {
    final int[] arr = {2, 4, 5};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertTrue(checker.hasCompositeParallel(1));
  }

  @Test
  public void testParallelWithThreadCountEqualLength() {
    final int[] arr = {2, 3, 5, 7, 11};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertFalse(checker.hasCompositeParallel(5));
  }

  @Test
  public void testParallelWithThreadCountLargerThanLength() {
    final int[] arr = {2, 3};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertFalse(checker.hasCompositeParallel(10));
  }

  @Test
  public void testParallelStream() {
    final int[] arr = {2, 3, 5, 7, 11};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertFalse(checker.hasCompositeParallelStream());

    final int[] arr2 = {2, 4, 5};
    final PrimeChecker checker2 = new PrimeChecker(arr2);
    assertTrue(checker2.hasCompositeParallelStream());
  }

  @Test
  public void testParallelStreamSingleComposite() {
    final int[] arr = {4};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertTrue(checker.hasCompositeParallelStream());
  }

  @Test
  public void testParallelStreamSinglePrime() {
    final int[] arr = {2};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertFalse(checker.hasCompositeParallelStream());
  }

  @Test
  public void testEmptyArray() {
    final int[] empty = {};
    final PrimeChecker checker = new PrimeChecker(empty);
    assertFalse(checker.hasCompositeSequential());
    assertFalse(checker.hasCompositeParallel(2));
    assertFalse(checker.hasCompositeParallelStream());
  }

  @Test
  public void testLargeNumbers() {
    final int[] arr = {1000000007, 1000000009};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertFalse(checker.hasCompositeSequential());
    assertFalse(checker.hasCompositeParallel(2));
    assertFalse(checker.hasCompositeParallelStream());
  }

  @Test
  public void testLargeNumbersWithComposite() {
    final int[] arr = {1000000006, 1000000009};
    final PrimeChecker checker = new PrimeChecker(arr);
    assertTrue(checker.hasCompositeSequential());
    assertTrue(checker.hasCompositeParallel(2));
    assertTrue(checker.hasCompositeParallelStream());
  }
}

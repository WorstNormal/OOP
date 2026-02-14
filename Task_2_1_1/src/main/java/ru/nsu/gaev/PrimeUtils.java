package ru.nsu.gaev;

/**
 * Utility class for prime number operations.
 */
public class PrimeUtils {
  /**
   * Checks if a number is prime.
   *
   * @param num the number to check
   * @return true if prime, false otherwise
   */
  public static boolean isPrime(int num) {
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
}

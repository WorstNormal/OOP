package ru.nsu.gaev.strategy;

import ru.nsu.gaev.PrimeUtils;

/**
 * Sequential execution strategy.
 */
public class SequentialStrategy implements PrimeCheckStrategy {
  @Override
  public boolean hasComposite(int[] array) {
    for (int num : array) {
      if (!PrimeUtils.isPrime(num)) {
        return true;
      }
    }
    return false;
  }
}

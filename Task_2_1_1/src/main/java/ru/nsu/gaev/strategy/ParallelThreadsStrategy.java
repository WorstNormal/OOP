package ru.nsu.gaev.strategy;

import ru.nsu.gaev.PrimeUtils;

/**
 * Parallel execution strategy using java.lang.Thread.
 */
public class ParallelThreadsStrategy implements PrimeCheckStrategy {

    private int threadCount;

    private volatile boolean foundComposite;

    public ParallelThreadsStrategy(int threadCount) {
        this.threadCount = threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    @Override
    public boolean hasComposite(int[] array) {
        foundComposite = false;

        // Protection from invalid input data
        if (array == null || array.length == 0) {
            return false;
        }

        int currentThreads = threadCount;
        if (currentThreads <= 0) {
            currentThreads = 1;
        }

        // If threads > elements, reduce thread count
        if (array.length < currentThreads) {
            currentThreads = array.length;
        }

        // Create and start threads
        final Thread[] threads = new Thread[currentThreads];
        final int elementsPerThread = array.length / currentThreads;
        final int remainder = array.length % currentThreads;

        for (int t = 0; t < currentThreads; t++) {
            final int startIdx = t * elementsPerThread + Math.min(t, remainder);
            final int endIdx = startIdx + elementsPerThread + (t < remainder ? 1 : 0);

            threads[t] = new Thread(() -> {
                // Each thread checks its range
                for (int i = startIdx; i < endIdx; i++) {
                    if (foundComposite) {
                        return; // Optimization
                    }
                    if (!PrimeUtils.isPrime(array[i])) {
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
}

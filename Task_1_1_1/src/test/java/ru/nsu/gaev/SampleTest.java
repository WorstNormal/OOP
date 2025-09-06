package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Класс тестирования для алгоритма сортировки.
 */
class SampleTest {

    /**
     * Тестирует правильность сортировки с использованием метода heapsort.
     */
    @Test
    void checkSorting() {
        int[] array = {4, 10, 3, 5, 1};
        int[] expected = {1, 3, 4, 5, 10};
        int[] result = new Sample().heapsort(array);
        assertArrayEquals(expected, result, () -> "Ошибка с стандартным массивом");
    }

    /**
     * Тестирует работу метода heapsort с пустым массивом.
     */
    @Test
    void checkEmptyArray() {
        int[] array = {};
        int[] result = new Sample().heapsort(array);
        assertArrayEquals(new int[]{}, result, () -> "Ошибка с пустым массивом");
    }

    /**
     * Тестирует работу метода heapsort с массивом из одного элемента.
     */
    @Test
    void checkSingleElement() {
        int[] array = {42};
        int[] result = new Sample().heapsort(array);
        assertArrayEquals(new int[]{42}, result, () -> "Ошибка с массивом из одного элемента");
    }

    /**
     * Тестирует работу метода heapsort с большим массивом.
     */
    @Test
    void checkLargeLen() {
        int[] array = new int[1000];
        IntStream.range(0, array.length).forEach(i -> array[i] = array.length - i);
        int[] result = new Sample().heapsort(array);
        for (int i = 1; i < result.length; i++) {
            assertTrue(result[i - 1] <= result[i], "Массив не отсортирован");
        }
    }
}

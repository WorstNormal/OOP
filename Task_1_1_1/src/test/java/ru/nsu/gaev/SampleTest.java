package ru.nsu.gaev;

import org.junit.jupiter.api.Test;
import java.util.stream.IntStream;
import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    @Test
    void checkSorting() { // все ли работает
        int[] array = {4, 10, 3, 5, 1};
        int[] expected = {1, 3, 4, 5, 10};
        int[] result = new Sample().heapSort(array);
        assertArrayEquals(expected, result, () -> "Ошибка с стандартным массивом");
    }

    @Test
    void checkEmptyArray() { // крайний случай снизу 1
        int[] array = {};
        int[] result = new Sample().heapSort(array);
        assertArrayEquals(new int[]{}, result, () -> "Ошибка с пустым массивом");
    }

    @Test
    void checkSingleElement() { // крайний случай снизу 2
        int[] array = {42};
        int[] result = new Sample().heapSort(array);
        assertArrayEquals(new int[]{42}, result, () ->  "Ошибка с массивом из одного элемента");
    }

    @Test
    void checkLargeLen() { // крайний случай сверху по количеству
        int[] array = IntStream.concat(
                IntStream.of(2),
                IntStream.generate(() -> 1).limit(10000)
        ).toArray();
        int[] result = new Sample().heapsort(array);
        int[] expected = IntStream.concat(
                IntStream.generate(() -> 1).limit(10000),
                IntStream.of(2)
        ).toArray();
        assertArrayEquals(expected, result, () -> "Ошибка с большим количеством данных");
    }

    @Test
    void checkLargeElements() { // крайний случай сверху по введенным данным
        int[] array = IntStream.concat(
                IntStream.generate(() -> (int) 1e9).limit(100),
                IntStream.of(2)
        ).toArray();
        int[] result = new Sample().heapsort(array);
        int[] expected = IntStream.concat(
                IntStream.of(2),
                IntStream.generate(() -> (int) 1e9).limit(100)
        ).toArray();
        assertArrayEquals(expected, result, () -> "Ошибка с большим количеством данных");
    }
}

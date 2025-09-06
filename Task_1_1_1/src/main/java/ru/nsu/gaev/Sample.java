package ru.nsu.gaev;

import java.util.Arrays;

/**
 * Класс, реализующий алгоритм сортировки методом кучи (heapsort).
 */
public class Sample {

    /**
     * Основной метод, запускающий приложение.
     */
    public static void main(String[] args) {
        System.out.println("Приложение запущено!");
    }

    /**
     * Сортирует массив с использованием алгоритма heapsort.
     *
     * @param array Массив целых чисел для сортировки.
     * @return Отсортированный массив.
     */
    public int[] heapsort(int[] array) {
        int[] heapArray = Arrays.copyOf(array, array.length);
        int n = heapArray.length;

        // Построение кучи
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(heapArray, n, i);
        }

        // Извлечение элементов из кучи
        for (int i = n - 1; i > 0; i--) {
            int temp = heapArray[0];
            heapArray[0] = heapArray[i];
            heapArray[i] = temp;
            heapify(heapArray, i, 0);
        }

        return heapArray;
    }

    /**
     * Восстанавливает структуру кучи в поддереве с корнем в индексе i.
     *
     * @param arr Массив, который представляет кучу.
     * @param n Размер массива.
     * @param i Индекс корня поддерева.
     */
    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // Если левый дочерний элемент больше корня
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // Если правый дочерний элемент больше самого большого
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // Если самый большой элемент не корень
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Рекурсивно восстанавливаем кучу
            heapify(arr, n, largest);
        }
    }
}

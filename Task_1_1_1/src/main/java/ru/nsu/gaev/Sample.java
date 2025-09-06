package ru.nsu.gaev;

/**
 * Класс, содержащий метод сортировки массива с помощью кучи и точку входа для приложения.
 */
public class Sample {

    /**
     * Главный метод, который запускает приложение.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        System.out.println("Приложение запущено!");
    }

    /**
     * Сортировка массива с использованием алгоритма heapsort.
     *
     * @param array массив для сортировки
     * @return отсортированный массив
     */
    public int[] heapSort(int[] array) {
        // Реализация алгоритма сортировки кучей (heapsort)
        int n = array.length;

        // Строим кучу (heap)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }

        // Извлекаем элементы из кучи
        for (int i = n - 1; i >= 0; i--) {
            // Перемещаем текущий корень в конец
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;

            // Вызываем heapify на уменьшенной куче
            heapify(array, i, 0);
        }

        return array;
    }

    /**
     * Вспомогательный метод для восстановления свойств кучи.
     *
     * @param array массив, представляющий кучу
     * @param n размер кучи
     * @param i индекс текущего узла
     */
    private void heapify(int[] array, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        // Если левый дочерний элемент больше корня
        if (left < n && array[left] > array[largest]) {
            largest = left;
        }

        // Если правый дочерний элемент больше самого большого элемента
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }

        // Если largest не является корнем
        if (largest != i) {
            int swap = array[i];
            array[i] = array[largest];
            array[largest] = swap;

            // Рекурсивно восстанавливаем свойства кучи
            heapify(array, n, largest);
        }
    }
}

package ru.nsu.gaev;
import java.util.Arrays;

public class Sample {
    public static void main(String[] args) {
        System.out.println("Приложение запущено!");
    }
    public int[] heapsort(int[] array) {
        int[] heapArray = Arrays.copyOf(array, array.length);
        int n = heapArray.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(heapArray, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            int temp = heapArray[0];
            heapArray[0] = heapArray[i];
            heapArray[i] = temp;
            heapify(heapArray, i, 0);
        }

        return heapArray;
    }
    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest);
        }
    }
}

package ru.nsu.gaev;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий логику поиска подстроки алгоритмом Кнута-Морриса-Пратта (KMP).
 * Оптимизирован для потокового чтения (O(N) времени, O(M) памяти).
 */
public class KmpLogic {

    /**
     * Выполняет поиск подстроки в потоке данных.
     *
     * @param reader поток чтения файла (InputProvider)
     * @param pattern искомая строка
     * @return список индексов вхождений
     */
    public List<Long> findPattern(Reader reader, String pattern) throws IOException {
        List<Long> occurrences = new ArrayList<>();
        if (pattern == null || pattern.isEmpty()) {
            return occurrences;
        }

        int[] pi = computePrefixFunction(pattern);
        long globalIndex = 0;
        int j = 0; // Индекс в паттерне
        int nextChar;

        // Читаем посимвольно из буфера
        while ((nextChar = reader.read()) != -1) {
            char character = (char) nextChar;

            while (j > 0 && character != pattern.charAt(j)) {
                j = pi[j - 1];
            }

            if (character == pattern.charAt(j)) {
                j++;
            }

            if (j == pattern.length()) {
                occurrences.add(globalIndex - pattern.length() + 1);
                j = pi[j - 1];
            }

            globalIndex++;
        }

        // ВАЖНО: Мы НЕ закрываем reader здесь.
        // Его закроет try-with-resources в классе SearchApp.

        return occurrences;
    }

    /**
     * Вспомогательный метод для вычисления префикс-функции.
     */
    private int[] computePrefixFunction(String pattern) {
        int[] pi = new int[pattern.length()];
        for (int i = 1; i < pattern.length(); i++) {
            int j = pi[i - 1];
            while (j > 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = pi[j - 1];
            }
            if (pattern.charAt(i) == pattern.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        return pi;
    }
}
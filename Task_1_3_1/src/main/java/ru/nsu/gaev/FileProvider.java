package ru.nsu.gaev;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

/**
 * Класс, отвечающий за работу с файловой системой.
 * Предоставляет потоки чтения и генерирует тестовые данные.
 */
public class FileProvider {

    /**
     * Возвращает буферизированный Reader для файла с кодировкой UTF-8.
     * @param fileName имя файла
     */
    public static BufferedReader getReader(String fileName) throws FileNotFoundException {
        return new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8)
        );
    }

    /**
     * Генерирует большой тестовый файл, как требовалось в условии (Big Data simulation).
     */
    public static void generateBigFile(String fileName, String patternToInsert) throws IOException {
        Random random = new Random();
        int sizeMB = 10; // Размер ~10 МБ
        long charsToWrite = sizeMB * 1024L * 1024L;

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {

            for (long i = 0; i < charsToWrite; i++) {
                // С вероятностью ~0.001% вставляем искомую подстроку
                if (random.nextInt(100000) == 42) {
                    writer.write(patternToInsert);
                    i += patternToInsert.length() - 1;
                } else {
                    // Случайный символ (русские буквы для теста UTF-8)
                    char c = (char) (random.nextInt(32) + 'а');
                    writer.write(c);
                }

                // Периодический сброс буфера для экономии памяти
                if (i % 8192 == 0) writer.flush();
            }
        }
    }
}
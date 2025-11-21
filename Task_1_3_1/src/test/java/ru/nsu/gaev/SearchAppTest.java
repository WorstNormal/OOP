package ru.nsu.gaev;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса SearchApp (основной цикл приложения).
 */
class SearchAppTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private InputStream originalIn;
    private static final String TEST_INPUT_FILE = "input.txt";

    @BeforeEach
    void setUp() throws IOException {
        // Перехват вывода
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        // Перехват ввода
        originalIn = System.in;

        // Создаем дефолтный файл для теста
        Files.writeString(Path.of(TEST_INPUT_FILE), "test data");
    }

    @AfterEach
    void tearDown() throws IOException {
        System.setOut(originalOut);
        System.setIn(originalIn);
        Files.deleteIfExists(Path.of(TEST_INPUT_FILE));
        Files.deleteIfExists(Path.of("large_test_input.txt"));
    }

    /**
     * Проверяет полный сценарий:
     * 1. Выбор существующего файла (input)
     * 2. Ввод имени файла (Enter -> default)
     * 3. Ввод паттерна
     * 4. Отказ от повторной игры
     */
    @Test
    void mainFlowTest() {
        // Сценарий ввода: "input" -> "" (Enter для дефолтного файла) -> "data" (паттерн) -> "no" (выход)
        String input = String.join("\n", "input", "", "data", "no") + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SearchApp.main(new String[]{});

        String output = outputStream.toString();

        assertTrue(output.contains("Welcome to Substring Search App"));
        assertTrue(output.contains("Search started..."));
        assertTrue(output.contains("Total occurrences found:"));
        assertTrue(output.contains("Exiting application."));
    }

    /**
     * Проверяет сценарий генерации файла.
     */
    @Test
    void generateFlowTest() {
        // Сценарий: "generate" -> "бра" (паттерн) -> "no"
        String input = String.join("\n", "generate", "бра", "no") + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SearchApp.main(new String[]{});

        String output = outputStream.toString();

        assertTrue(output.contains("Generating large file"));
        assertTrue(output.contains("File generation complete"));
    }

    /**
     * Проверяет обработку неверного ввода при вопросе "play again".
     */
    @Test
    void invalidInputTest() {
        // Сценарий: "input" -> "" -> "abc" -> "bla" (ошибка) -> "no"
        String input = String.join("\n", "input", "", "abc", "bla", "no") + "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        SearchApp.main(new String[]{});

        String output = outputStream.toString();

        assertTrue(output.contains("Please enter 'yes' or 'no'"));
    }
}
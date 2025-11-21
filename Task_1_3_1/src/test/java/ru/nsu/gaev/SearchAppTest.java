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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Расширенные тесты для SearchApp.
 */
class SearchAppTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private PrintStream originalErr; // Добавили сохранение оригинального err
    private InputStream originalIn;

    private static final String DEFAULT_FILE = "input.txt";
    private static final String CUSTOM_FILE = "custom_test.txt";
    private static final String MISSING_FILE = "missing_file_123.txt";

    @BeforeEach
    void setUp() throws IOException {
        // ВАЖНО: Ускоряем тесты, генерируем только 1 МБ данных
        FileProvider.GENERATION_SIZE_MB = 1;

        // Перехват вывода (System.out)
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Перехватываем System.err
        originalErr = System.err;
        System.setErr(new PrintStream(outputStream));

        // Перехват ввода
        originalIn = System.in;

        // Создаем файлы для тестов
        Files.writeString(Path.of(DEFAULT_FILE), "default data content");
        Files.writeString(Path.of(CUSTOM_FILE), "custom file content with secret");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Возвращаем потоки на место
        System.setOut(originalOut);
        System.setIn(originalIn);
        System.setErr(originalErr); // Корректный возврат

        // Возвращаем размер файла для реальной работы
        FileProvider.GENERATION_SIZE_MB = 200;

        // Чистим мусор
        Files.deleteIfExists(Path.of(DEFAULT_FILE));
        Files.deleteIfExists(Path.of(CUSTOM_FILE));
        Files.deleteIfExists(Path.of("large_test_input.txt"));
    }

    /**
     * Тест 1: Стандартный поток (Default файл).
     */
    @Test
    void testDefaultFileFlow() {
        String input = String.join("\n", "input", "", "data", "no") + "\n";
        provideInput(input);

        SearchApp.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Search finished"), "Поиск должен успешно завершиться");
        assertTrue(output.contains("Exiting application"), "Приложение должно корректно выйти");
    }

    /**
     * Тест 2: Пользовательский файл.
     */
    @Test
    void testCustomFileName() {
        String input = String.join("\n", "input", CUSTOM_FILE, "secret", "no") + "\n";
        provideInput(input);

        SearchApp.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Total occurrences found: 1"), "Должен найти 'secret' в кастомном файле");
    }

    /**
     * Тест 3: Обработка IOException (Файл не найден).
     */
    @Test
    void testFileNotFoundError() {
        String input = String.join("\n", "input", MISSING_FILE, "any", "no") + "\n";
        provideInput(input);

        SearchApp.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Error occurred"), "Должно быть сообщение об ошибке");
        assertTrue(output.contains("Perform another search?"), "Приложение должно предложить повторить после ошибки");
    }

    /**
     * Тест 4: Генерация файла.
     */
    @Test
    void testGenerateFlow() {
        String input = String.join("\n", "generate", "бра", "no") + "\n";
        provideInput(input);

        SearchApp.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Generating large file"), "Должно начаться создание файла");
        assertTrue(output.contains("Search finished"), "Поиск должен пройти по сгенерированному файлу");
    }

    /**
     * Тест 5: Цикл игры (Play Again = Yes).
     */
    @Test
    void testPlayAgainLoop() {
        String round1 = String.join("\n", "input", "", "data");
        String choiceBetween = "yes";
        String round2 = String.join("\n", "input", CUSTOM_FILE, "secret");
        String end = "no";

        String fullInput = round1 + "\n" + choiceBetween + "\n" + round2 + "\n" + end + "\n";
        provideInput(fullInput);

        SearchApp.main(new String[]{});

        String output = outputStream.toString();

        int startCount = output.split("Search started").length - 1;
        assertTrue(startCount >= 2, "Поиск должен быть выполнен минимум 2 раза");
    }

    /**
     * Тест 6: Валидация ввода "Play again".
     */
    @Test
    void testInvalidYesNoInput() {
        String input = String.join("\n", "input", "", "abc", "idk", "no") + "\n";
        provideInput(input);

        SearchApp.main(new String[]{});

        String output = outputStream.toString();
        assertTrue(output.contains("Please enter 'yes' or 'no'"), "Должно быть предупреждение о неверном вводе");
    }

    /**
     * Тест 7: Обрыв ввода (EOF).
     */
    @Test
    void testUnexpectedEndOfInput() {
        provideInput("input");

        SearchApp.main(new String[]{});

        String output = outputStream.toString();
        assertFalse(output.contains("Exiting application"), "Может не успеть дойти до штатного выхода, но не должно падать");
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }
}
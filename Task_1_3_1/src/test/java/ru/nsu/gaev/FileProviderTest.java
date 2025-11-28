package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса FileProvider (работа с файловой системой).
 */
class FileProviderTest {
    private static final String TEST_FILENAME = "unit_test_file.txt";
    private static final String BIG_FILENAME = "large_test_unit.txt";

    @BeforeEach
    void setUp() throws IOException {
        // ВАЖНО: Для тестов ставим размер 1 МБ, чтобы тест не вис
        FileProvider.GENERATION_SIZE_MB = 1;

        // Создаем маленький тестовый файл
        Files.writeString(Path.of(TEST_FILENAME), "test content");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Возвращаем стандартный размер на всякий случай
        FileProvider.GENERATION_SIZE_MB = 200;

        // Удаляем файлы после теста
        Files.deleteIfExists(Path.of(TEST_FILENAME));
        Files.deleteIfExists(Path.of(BIG_FILENAME));
    }

    /**
     * Проверяет получение BufferedReader для существующего файла.
     */
    @Test
    void getReaderTest() throws IOException {
        BufferedReader reader = FileProvider.getReader(TEST_FILENAME);
        assertNotNull(reader);
        assertEquals("test content", reader.readLine());
        reader.close();
    }

    /**
     * Проверяет выброс исключения для несуществующего файла.
     */
    @Test
    void getReaderNotFoundTest() {
        assertThrows(FileNotFoundException.class,
                () -> FileProvider.getReader("non_existent_file_999.txt")
        );
    }

    /**
     * Проверяет генерацию большого файла.
     */
    @Test
    void generateBigFileTest() throws IOException {
        FileProvider.generateBigFile(BIG_FILENAME, "abc");

        File file = new File(BIG_FILENAME);
        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        // Проверяем, что файл читается
        try (BufferedReader reader = FileProvider.getReader(BIG_FILENAME)) {
            assertNotNull(reader.readLine());
        }
    }
}
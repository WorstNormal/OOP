package ru.nsu.gaev;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class MainTest {

    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        // Восстанавливаем оригинальные System.out и System.err
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    /**
     * Тест для успешного выполнения main() с корректным файлом графа.
     */
    @Test
    void testMain_Success(@TempDir Path tempDir) throws IOException {
        String fileContent =
                "4\n"
                +
                "A\nB\nC\nD\n"
                +
                "3\n"
                +
                "A B\n"
                +
                "A C\n"
                +
                "C D\n";

        Path graphFile = tempDir.resolve("graph.txt");
        Files.writeString(graphFile, fileContent);
        Path originalUserDir = Path.of(System.getProperty("user.dir"));
        System.setProperty("user.dir", tempDir.toAbsolutePath().toString());

        try {
            Main.main(new String[]{});
            assertEquals("", errorStreamCaptor.toString().trim(),
                    "Ожидалось, что ошибок не будет.");

            String actualOutput = outputStreamCaptor.toString();
            assertTrue(actualOutput.contains("List vs Matrix equals: true"),
                    "Должно быть найдено сравнение equals.");
            assertTrue(actualOutput.contains("Topological Sort"),
                    "Должен быть найден заголовок сортировки.");

        } finally {
            System.setProperty("user.dir", originalUserDir.toString());
        }
    }
}
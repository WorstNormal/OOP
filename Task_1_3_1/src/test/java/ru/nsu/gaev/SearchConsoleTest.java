package ru.nsu.gaev;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса SearchConsole (вывод сообщений).
 */
class SearchConsoleTest {
    private final SearchConsole console = new SearchConsole();
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Проверяет вывод результатов, когда паттерн найден.
     */
    @Test
    void printResultsFoundTest() {
        console.printResults(List.of(1L, 5L, 10L));
        String output = outputStream.toString();
        assertTrue(output.contains("Total occurrences found: 3"));
        assertTrue(output.contains("[1, 5, 10]"));
    }

    /**
     * Проверяет вывод, когда паттерн не найден.
     */
    @Test
    void printResultsNotFoundTest() {
        console.printResults(List.of());
        String output = outputStream.toString();
        assertTrue(output.contains("Pattern not found."));
    }

    /**
     * Проверяет вывод сообщения об ошибке.
     */
    @Test
    void errorMessageTest() {
        console.errorMessage("File not found");
    }

    /**
     * Проверяет приветственное сообщение.
     */
    @Test
    void welcomeMessageTest() {
        console.welcomeSearch();
        assertTrue(outputStream.toString().contains("Welcome"));
    }
}
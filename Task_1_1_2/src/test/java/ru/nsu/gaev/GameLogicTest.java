package ru.nsu.gaev;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса GameLogic (основная логика игры Blackjack).
 */
class GameLogicTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private InputStream originalIn;

    /**
     * Подготавливает перехват стандартных потоков ввода-вывода перед каждым тестом.
     */
    @BeforeEach
    void setUp() {
        // Перехват вывода
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // Перехват ввода
        originalIn = System.in;
    }

    /**
     * Проверяет, что при некорректном вводе ответа на вопрос о продолжении игры,
     * появляется соответствующее сообщение.
     */
    @Test
    void mainTest() {
        // Подготовка ввода для теста (вводим 1 для количества колод, затем "stand", потом некорректный ввод "sad", потом правильный ввод "no")
        String input = "1\nstand\nsad\nno\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Запускаем метод main()
        String[] gameArgs = {};
        GameLogic.main(gameArgs);

        // Захватываем вывод
        String output = outputStream.toString();

        // Проверяем, что запрос на корректный ввод был сделан
        assertTrue(output.contains("Please enter 'yes' or 'no':"));
    }

    /**
     * Восстанавливает стандартные потоки после теста.
     */
    @AfterEach
    void restoreSystemStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
}

package ru.nsu.gaev;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для класса KmpLogic (реализация алгоритма КМП).
 */
class KmpLogicTest {
    private final KmpLogic kmpLogic = new KmpLogic();

    /**
     * Проверяет стандартный сценарий поиска с двумя вхождениями.
     * Вход: "абракадабра", Паттерн: "бра"
     * Ожидание: индексы 1 и 8.
     */
    @Test
    void findPatternStandardTest() throws IOException {
        String text = "абракадабра";
        String pattern = "бра";
        StringReader reader = new StringReader(text);

        List<Long> result = kmpLogic.findPattern(reader, pattern);

        assertEquals(List.of(1L, 8L), result);
    }

    /**
     * Проверяет случай, когда вхождений нет.
     */
    @Test
    void findPatternNoMatchTest() throws IOException {
        String text = "hello world";
        String pattern = "java";
        StringReader reader = new StringReader(text);

        List<Long> result = kmpLogic.findPattern(reader, pattern);

        assertTrue(result.isEmpty());
    }

    /**
     * Проверяет поиск с перекрывающимися вхождениями.
     * Вход: "nanana", Паттерн: "nana"
     * Ожидание: индексы 0 и 2.
     */
    @Test
    void findPatternOverlappingTest() throws IOException {
        String text = "nanana";
        String pattern = "nana";
        StringReader reader = new StringReader(text);

        List<Long> result = kmpLogic.findPattern(reader, pattern);

        // 0: [nana]na
        // 2: na[nana]
        assertEquals(List.of(0L, 2L), result);
    }

    /**
     * Проверяет работу с UTF-8 символами (эмодзи).
     */
    @Test
    void findPatternUtf8Test() throws IOException {
        String text = "Java🔥Hot🔥";
        String pattern = "🔥";
        StringReader reader = new StringReader(text);

        List<Long> result = kmpLogic.findPattern(reader, pattern);

        assertEquals(List.of(4L, 8L), result);
    }

    /**
     * Проверяет поведение при пустом паттерне.
     */
    @Test
    void findPatternEmptyTest() throws IOException {
        String text = "abc";
        String pattern = "";
        StringReader reader = new StringReader(text);

        List<Long> result = kmpLogic.findPattern(reader, pattern);

        assertTrue(result.isEmpty());
    }
}
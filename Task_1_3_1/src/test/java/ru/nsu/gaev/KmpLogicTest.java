package ru.nsu.gaev;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class KmpLogicTest {

    @Test
    void testStandardCase() throws IOException {
        KmpLogic logic = new KmpLogic();
        String text = "абракадабра";
        String pattern = "бра";
        StringReader reader = new StringReader(text);

        List<Long> result = logic.findPattern(reader, pattern);
        Assertions.assertEquals(List.of(1L, 8L), result);
    }

    @Test
    void testPatternAtStart() throws IOException {
        KmpLogic logic = new KmpLogic();
        String text = "hello world";
        String pattern = "hello";

        StringReader reader = new StringReader(text);
        List<Long> result = logic.findPattern(reader, pattern);

        Assertions.assertEquals(List.of(0L), result);
    }

    @Test
    void testPatternAtEnd() throws IOException {
        KmpLogic logic = new KmpLogic();
        String text = "hello world";
        String pattern = "world";

        StringReader reader = new StringReader(text);
        List<Long> result = logic.findPattern(reader, pattern);

        Assertions.assertEquals(List.of(6L), result);
    }

    @Test
    void testNoMatches() throws IOException {
        KmpLogic logic = new KmpLogic();
        String text = "abcdefg";
        String pattern = "xyz";

        StringReader reader = new StringReader(text);
        List<Long> result = logic.findPattern(reader, pattern);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testOverlappingPatterns() throws IOException {
        KmpLogic logic = new KmpLogic();
        String text = "ababa";
        String pattern = "aba";
        StringReader reader = new StringReader(text);
        List<Long> result = logic.findPattern(reader, pattern);

        Assertions.assertEquals(List.of(0L, 2L), result);
    }

    @Test
    void testEmptyInput() throws IOException {
        KmpLogic logic = new KmpLogic();
        String text = "";
        String pattern = "test";

        StringReader reader = new StringReader(text);
        List<Long> result = logic.findPattern(reader, pattern);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testUtf8Emoji() throws IOException {
        KmpLogic logic = new KmpLogic();
        String text = "test!text!";
        String pattern = "!";

        StringReader reader = new StringReader(text);
        List<Long> result = logic.findPattern(reader, pattern);
        Assertions.assertEquals(2, result.size());
    }
}
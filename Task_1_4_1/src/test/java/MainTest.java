package ru.nsu.gaev;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void testMain() {
        // Просто запускаем main, чтобы убедиться, что он не падает с ошибкой
        // и чтобы покрыть класс тестом.
        Main.main(new String[]{});
    }
}
package ru.nsu.gaev.model;

import org.junit.jupiter.api.Test;
import ru.nsu.gaev.Main;

/**
 * Тест, проверяющий, что демонстрационный метод main выполняется без ошибок.
 */
public class MainTest {

    @Test
    public void testMainRuns() {
        
        Main.main(new String[]{});
    }
}

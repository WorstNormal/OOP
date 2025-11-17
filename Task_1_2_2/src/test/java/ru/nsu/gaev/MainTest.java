package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ConcurrentModificationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Main class entry point.
 * Checks standard output and expected exception behavior.
 */
class MainTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    /**
     * Redirects System.out to a ByteArrayOutputStream to capture output.
     */
    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    /**
     * Restores the original System.out after each test.
     */
    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    /**
     * Verifies that Main.main executes expected logic and throws ConcurrentModificationException.
     * The exception is expected because the main method modifies the map during iteration.
     */
    @Test
    @DisplayName("Test Main output and expected ConcurrentModificationException")
    void testMainExecution() {
        // Main.java intentionally causes a crash inside the loop.
        // We assert that the exception is thrown.
        assertThrows(ConcurrentModificationException.class, () -> {
            Main.main(new String[]{});
        });

        // We verify the output that occurred BEFORE the crash.
        final String output = outContent.toString();

        // Check put and get
        assertTrue(output.contains("After put('one', 1): 1"),
                "Output should contain initial put result");

        // Check update
        assertTrue(output.contains("After update('one', 1.0): 1.0"),
                "Output should contain update result");

        // Check remove logic
        assertTrue(output.contains("Does it contain key 'two': true"),
                "Output should confirm key existence");
        assertTrue(output.contains("Does it contain key 'two' after removal: false"),
                "Output should confirm key removal");

        // Check initial table state printing
        assertTrue(output.contains("Table state: {one=1.0}"),
                "Output should show table state before iteration");

        // Check entry into the dangerous section
        assertTrue(output.contains("Attempting to modify during iteration..."),
                "Output should indicate start of iteration");
    }
}
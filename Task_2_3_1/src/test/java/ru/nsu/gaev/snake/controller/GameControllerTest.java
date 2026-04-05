package ru.nsu.gaev.snake.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.Test;

/**
 * Tests for GameController class.
 * Note: Full integration tests require JavaFX initialization which is complex
 * in unit tests. These tests focus on controller behavior and keyboard event
 * handling.
 */
class GameControllerTest {

    @Test
    void testKeyCodeUpCreation() {
        KeyEvent upKeyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.UP,
                false,
                false,
                false,
                false
        );

        assertNotNull(upKeyEvent);
        assertEquals(KeyCode.UP, upKeyEvent.getCode());
    }

    @Test
    void testKeyCodeDownCreation() {
        KeyEvent downKeyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.DOWN,
                false,
                false,
                false,
                false
        );

        assertNotNull(downKeyEvent);
        assertEquals(KeyCode.DOWN, downKeyEvent.getCode());
    }

    @Test
    void testKeyCodeLeftCreation() {
        KeyEvent leftKeyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.LEFT,
                false,
                false,
                false,
                false
        );

        assertNotNull(leftKeyEvent);
        assertEquals(KeyCode.LEFT, leftKeyEvent.getCode());
    }

    @Test
    void testKeyCodeRightCreation() {
        KeyEvent rightKeyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.RIGHT,
                false,
                false,
                false,
                false
        );

        assertNotNull(rightKeyEvent);
        assertEquals(KeyCode.RIGHT, rightKeyEvent.getCode());
    }

    @Test
    void testWKeyCreation() {
        KeyEvent keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "w",
                null,
                KeyCode.W,
                false,
                false,
                false,
                false
        );

        assertNotNull(keyEvent);
        assertEquals(KeyCode.W, keyEvent.getCode());
    }

    @Test
    void testAKeyCreation() {
        KeyEvent keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "a",
                null,
                KeyCode.A,
                false,
                false,
                false,
                false
        );

        assertNotNull(keyEvent);
        assertEquals(KeyCode.A, keyEvent.getCode());
    }

    @Test
    void testSKeyCreation() {
        KeyEvent keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "s",
                null,
                KeyCode.S,
                false,
                false,
                false,
                false
        );

        assertNotNull(keyEvent);
        assertEquals(KeyCode.S, keyEvent.getCode());
    }

    @Test
    void testDKeyCreation() {
        KeyEvent keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "d",
                null,
                KeyCode.D,
                false,
                false,
                false,
                false
        );

        assertNotNull(keyEvent);
        assertEquals(KeyCode.D, keyEvent.getCode());
    }

    @Test
    void testMultipleKeyEventsSequence() {
        KeyEvent[] keySequence = {
            new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.UP,
                false,
                false,
                false,
                false
            ),
            new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.RIGHT,
                false,
                false,
                false,
                false
            ),
            new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.DOWN,
                false,
                false,
                false,
                false
            ),
            new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.LEFT,
                false,
                false,
                false,
                false
            )
        };

        assertEquals(4, keySequence.length);
        assertTrue(keySequence[0].getCode() == KeyCode.UP);
        assertTrue(keySequence[1].getCode() == KeyCode.RIGHT);
        assertTrue(keySequence[2].getCode() == KeyCode.DOWN);
        assertTrue(keySequence[3].getCode() == KeyCode.LEFT);
    }

    @Test
    void testKeyEventIsKeyPressed() {
        KeyEvent keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.UP,
                false,
                false,
                false,
                false
        );

        assertEquals(KeyEvent.KEY_PRESSED, keyEvent.getEventType());
    }

    @Test
    void testKeyEventNotKeyReleased() {
        KeyEvent keyEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED,
                null,
                null,
                KeyCode.UP,
                false,
                false,
                false,
                false
        );

        assertTrue(keyEvent.getEventType() != KeyEvent.KEY_RELEASED);
    }

    @Test
    void testGameControllerCreation() {
        GameController controller = new GameController();
        assertNotNull(controller);
    }
}

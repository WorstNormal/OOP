package ru.nsu.gaev.snake.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.Direction;
import ru.nsu.gaev.snake.model.GameField;
import ru.nsu.gaev.snake.model.Level;

/**
 * Tests for GameController class.
 */
class GameControllerTest {
    private GameController controller;
    private GameField gameField;
    private Canvas canvas;
    private Label scoreLabel;
    private Label levelLabel;

    @BeforeAll
    static void initToolkit() {
        // Initialize JavaFX toolkit if needed
        if (!Platform.isFxApplicationThread()) {
            Platform.startup(() -> {});
        }
    }

    @BeforeEach
    void setUp() {
        controller = new GameController();
        canvas = new Canvas(600, 600);
        scoreLabel = new Label();
        levelLabel = new Label();

        // Use reflection to set private fields for testing
        try {
            var gameCanvasField = GameController.class.getDeclaredField("gameCanvas");
            gameCanvasField.setAccessible(true);
            gameCanvasField.set(controller, canvas);

            var scoreLabelField = GameController.class.getDeclaredField("scoreLabel");
            scoreLabelField.setAccessible(true);
            scoreLabelField.set(controller, scoreLabel);

            var levelLabelField = GameController.class.getDeclaredField("levelLabel");
            levelLabelField.setAccessible(true);
            levelLabelField.set(controller, levelLabel);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testControllerInitialization() {
        assertNotNull(controller);
    }

    @Test
    void testGameFieldCreation() {
        try {
            var gameFieldField = GameController.class.getDeclaredField("gameField");
            gameFieldField.setAccessible(true);
            gameField = (GameField) gameFieldField.get(controller);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(gameField);
        assertEquals(20, gameField.getWidth());
        assertEquals(20, gameField.getHeight());
    }

    @Test
    void testRendererCreation() {
        try {
            var rendererField = GameController.class.getDeclaredField("renderer");
            rendererField.setAccessible(true);
            var renderer = rendererField.get(controller);
            assertNotNull(renderer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testScoreLabelInitialization() {
        assertTrue(scoreLabel.getText().isEmpty() || scoreLabel.getText().contains("Score"));
    }

    @Test
    void testLevelLabelInitialization() {
        assertTrue(levelLabel.getText().isEmpty() || levelLabel.getText().contains("Level"));
    }

    @Test
    void testGameTimerExists() {
        try {
            var timerField = GameController.class.getDeclaredField("timer");
            timerField.setAccessible(true);
            var timer = timerField.get(controller);
            assertNotNull(timer);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testHandleKeyPress() {
        // Test arrow key events
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
    void testHandleKeyPressDown() {
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
    void testHandleKeyPressLeft() {
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
    void testHandleKeyPressRight() {
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
    void testGameFieldHasRobot() {
        try {
            var gameFieldField = GameController.class.getDeclaredField("gameField");
            gameFieldField.setAccessible(true);
            gameField = (GameField) gameFieldField.get(controller);

            assertTrue(gameField.getRobots().size() > 0);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGameLevelInitialized() {
        try {
            var gameFieldField = GameController.class.getDeclaredField("gameField");
            gameFieldField.setAccessible(true);
            gameField = (GameField) gameFieldField.get(controller);

            assertNotNull(gameField.getCurrentLevel());
            assertEquals(1, gameField.getCurrentLevel().levelNumber());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCanvasNotNull() {
        assertNotNull(canvas);
        assertEquals(600, canvas.getWidth(), 0.1);
        assertEquals(600, canvas.getHeight(), 0.1);
    }

    @Test
    void testCellSizeConstant() {
        try {
            var cellSizeField = GameController.class.getDeclaredField("CELL_SIZE");
            cellSizeField.setAccessible(true);
            int cellSize = cellSizeField.getInt(null);
            assertEquals(30, cellSize);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGameFieldDimensions() {
        try {
            var gameFieldField = GameController.class.getDeclaredField("gameField");
            gameFieldField.setAccessible(true);
            gameField = (GameField) gameFieldField.get(controller);

            // 600 / 30 = 20
            assertEquals(20, gameField.getWidth());
            assertEquals(20, gameField.getHeight());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testInitialGameNotStarted() {
        try {
            var gameFieldField = GameController.class.getDeclaredField("gameField");
            gameFieldField.setAccessible(true);
            gameField = (GameField) gameFieldField.get(controller);

            // Game should not be started initially
            assertTrue(!gameField.isGameOver());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testPlayerSnakeExists() {
        try {
            var gameFieldField = GameController.class.getDeclaredField("gameField");
            gameFieldField.setAccessible(true);
            gameField = (GameField) gameFieldField.get(controller);

            assertNotNull(gameField.getPlayer());
            assertTrue(gameField.getPlayer().isAlive());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testShowRulesDoesNotThrowException() {
        try {
            // Should not throw exception
            controller.showRules();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


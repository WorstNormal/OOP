package ru.nsu.gaev.snake.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.GameField;
import ru.nsu.gaev.snake.model.entity.Snake;

/**
 * Тесты для класса GameController.
 *
 * Полные интеграционные тесты требуют инициализации JavaFX, поэтому здесь
 * проверяется поведение контроллера и обработка клавиш.
 */
class GameControllerTest {
    private GameController controller;
    private GameField gameField;

    @BeforeEach
    void setUp() throws ReflectiveOperationException {
        controller = new GameController();
        gameField = new GameField(20, 20, 1, new Level(1, 10, 200L));
        setPrivateField(controller, "gameField", gameField);
    }

    private KeyEvent keyPress(KeyCode code) {
        return new KeyEvent(
                KeyEvent.KEY_PRESSED,
                "",
                "",
                code,
                false,
                false,
                false,
                false);
    }

    private void setPrivateField(Object target, String fieldName, Object value)
            throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void setGameFieldStateFlag(String flagName, boolean value)
            throws ReflectiveOperationException {
        Field field = gameField.getClass().getDeclaredField(flagName);
        field.setAccessible(true);
        field.setBoolean(gameField, value);
    }

    @Test
    void testControllerCreation() {
        assertNotNull(controller);
        assertNotNull(gameField);
    }

    @Test
    void testHandleKeyPressedStartsGameOnFirstValidInput() {
        assertFalse(gameField.isStarted());

        controller.handleKeyPressed(keyPress(KeyCode.RIGHT));

        assertTrue(gameField.isStarted());
    }

    @Test
    void testHandleKeyPressedDoesNotStartWhenGameIsOver()
            throws ReflectiveOperationException {
        setGameFieldStateFlag("gameOver", true);
        assertFalse(gameField.isStarted());

        controller.handleKeyPressed(keyPress(KeyCode.RIGHT));

        assertFalse(gameField.isStarted());
    }

    @Test
    void testHandleKeyPressedWMovesSnakeUp() {
        Snake player = gameField.getPlayer();
        Point initialHead = player.getHead();

        controller.handleKeyPressed(keyPress(KeyCode.W));
        player.move();

        assertEquals(new Point(initialHead.x(), initialHead.y() - 1),
                player.getHead());
        assertEquals(Direction.UP, player.getCurrentDirection());
    }

    @Test
    void testHandleKeyPressedSMapsToDown() {
        Snake player = gameField.getPlayer();
        Point initialHead = player.getHead();

        controller.handleKeyPressed(keyPress(KeyCode.S));
        player.move();

        assertEquals(new Point(initialHead.x(), initialHead.y() + 1),
                player.getHead());
        assertEquals(Direction.DOWN, player.getCurrentDirection());
    }

    @Test
    void testHandleKeyPressedAOrLeftMapsToLeft() {
        Snake player = gameField.getPlayer();
        Point initialHead = player.getHead();

        controller.handleKeyPressed(keyPress(KeyCode.LEFT));
        player.move();

        assertEquals(new Point(initialHead.x() - 1, initialHead.y()),
                player.getHead());
        assertEquals(Direction.LEFT, player.getCurrentDirection());
    }

    @Test
    void testHandleKeyPressedDOrRightMapsToRight() {
        Snake player = gameField.getPlayer();
        Point initialHead = player.getHead();

        controller.handleKeyPressed(keyPress(KeyCode.D));
        player.move();

        assertEquals(new Point(initialHead.x() + 1, initialHead.y()),
                player.getHead());
        assertEquals(Direction.RIGHT, player.getCurrentDirection());
    }

    @Test
    void testHandleKeyPressedIgnoresUnknownKeyDirection() {
        Snake player = gameField.getPlayer();
        Point initialHead = player.getHead();

        controller.handleKeyPressed(keyPress(KeyCode.SPACE));
        player.move();

        assertEquals(new Point(initialHead.x(), initialHead.y() - 1),
                player.getHead());
        assertEquals(Direction.UP, player.getCurrentDirection());
    }
}

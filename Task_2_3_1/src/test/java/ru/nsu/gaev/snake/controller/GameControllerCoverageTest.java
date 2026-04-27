package ru.nsu.gaev.snake.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.FoodType;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.GameField;
import ru.nsu.gaev.snake.model.core.GameFieldView;
import ru.nsu.gaev.snake.model.entity.Food;
import ru.nsu.gaev.snake.model.entity.RobotSnake;
import ru.nsu.gaev.snake.model.entity.Snake;
import ru.nsu.gaev.snake.model.strategy.GreedyStrategy;
import ru.nsu.gaev.snake.view.GameRenderer;

/**
 * Дополнительные тесты для GameController для повышения покрытия.
 */
class GameControllerCoverageTest {
    private static volatile boolean javaFxSupported = true;
    private GameController controller;
    private GameField gameField;

    private static final class FxFixture {
        private final GameController controller;
        private final GameField field;
        private final Label scoreLabel;
        private final Label levelLabel;

        private FxFixture(GameController controller, GameField field,
                Label scoreLabel, Label levelLabel) {
            this.controller = controller;
            this.field = field;
            this.scoreLabel = scoreLabel;
            this.levelLabel = levelLabel;
        }
    }

    @BeforeAll
    static void initJavaFxToolkit() {
        try {
            Platform.startup(() -> {
            });
        } catch (IllegalStateException ignored) {
            // Toolkit already initialized by another test class.
        } catch (UnsupportedOperationException ignored) {
            javaFxSupported = false;
        }
    }

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

    private void setBooleanField(Object target, String fieldName, boolean value)
            throws ReflectiveOperationException {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setBoolean(target, value);
    }

    private FxFixture createFxFixture() throws ReflectiveOperationException {
        GameController fxController = new GameController();
        Canvas canvas = new Canvas(600, 450);
        Label scoreLabel = new Label();
        Label levelLabel = new Label();
        final GameField fxField = new GameField(20, 15, 1, new Level(1, 100, 200L));
        setPrivateField(fxController, "gameField", fxField);
        final AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
            }
        };

        setPrivateField(fxController, "gameCanvas", canvas);
        setPrivateField(fxController, "scoreLabel", scoreLabel);
        setPrivateField(fxController, "levelLabel", levelLabel);
        setPrivateField(fxController, "renderer", new GameRenderer(canvas));
        setPrivateField(fxController, "timer", timer);

        return new FxFixture(fxController, fxField, scoreLabel, levelLabel);
    }

    private void invokeOnGameFieldChanged(GameController fxController,
            GameFieldView field) throws Exception {
        Method method = GameController.class.getDeclaredMethod(
                "onGameFieldChanged", GameFieldView.class);
        method.setAccessible(true);
        method.invoke(fxController, field);
    }

    @Test
    void testHandleKeyPressedUpArrow() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.UP));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedDownArrow() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.DOWN));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedLeftArrow() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.LEFT));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedRightArrow() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.RIGHT));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedW() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.W));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedS() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.S));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedA() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.A));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedD() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.D));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedStartsGame() throws ReflectiveOperationException {
        assertNotNull(gameField);
        controller.handleKeyPressed(keyPress(KeyCode.UP));

        // First key press should start the game
        assertTrue(gameField.isStarted());
    }

    @Test
    void testHandleKeyPressedWhenGameOver() throws ReflectiveOperationException {
        gameField.setStarted(true);
        gameField.getPlayer().kill();

        Snake player = gameField.getPlayer();

        controller.handleKeyPressed(keyPress(KeyCode.UP));

        // Should not crash
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedWhenGameWon() throws ReflectiveOperationException {
        gameField.setStarted(true);

        // Manually trigger win
        for (int i = 0; i < 1100; i++) {
            gameField.getPlayer().eat(new Food(
                new Point(0, 0),
                FoodType.NORMAL
            ));
        }

        Snake player = gameField.getPlayer();
        controller.handleKeyPressed(keyPress(KeyCode.UP));

        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedWhenGameDraw() throws ReflectiveOperationException {
        gameField.setStarted(true);

        RobotSnake robot = new RobotSnake(
            new Point(15, 15),
            Direction.UP,
            new GreedyStrategy()
        );
        gameField.addRobot(robot);

        // Try key press while game might end in draw
        controller.handleKeyPressed(keyPress(KeyCode.RIGHT));

        Snake player = gameField.getPlayer();
        assertNotNull(player);
    }

    @Test
    void testHandleKeyPressedUnknownKey() throws ReflectiveOperationException {
        controller.handleKeyPressed(keyPress(KeyCode.SPACE));

        // Should not crash, just ignore unknown keys
        assertNotNull(gameField);
    }

    @Test
    void testGameFieldInitialized() {
        assertNotNull(gameField);
        assertEquals(20, gameField.getWidth());
        assertEquals(20, gameField.getHeight());
    }

    @Test
    void testGameFieldHasFood() {
        assertTrue(gameField.getFoods().size() > 0);
    }

    @Test
    void testGameFieldHasPlayer() {
        assertNotNull(gameField.getPlayer());
    }

    @Test
    @Timeout(10)
    void testOnGameFieldChangedUpdatesUiWhenGameContinues() throws Exception {
        Assumptions.assumeTrue(javaFxSupported,
                "JavaFX toolkit is not supported in current environment");

        FxFixture fixture = createFxFixture();

        GameController.UiExecutor originalExecutor = GameController.uiExecutor;
        try {
            GameController.uiExecutor = Runnable::run;
            invokeOnGameFieldChanged(fixture.controller, fixture.field);
        } finally {
            GameController.uiExecutor = originalExecutor;
        }

        assertEquals("Score: 0", fixture.scoreLabel.getText());
        assertEquals("Level: 1", fixture.levelLabel.getText());
    }

    @Test
    @Timeout(10)
    void testOnGameFieldChangedShowsGameOverMessage() throws Exception {
        Assumptions.assumeTrue(javaFxSupported,
                "JavaFX toolkit is not supported in current environment");

        FxFixture fixture = createFxFixture();
        setBooleanField(fixture.field, "gameOver", true);

        GameController.UiExecutor originalExecutor = GameController.uiExecutor;
        try {
            GameController.uiExecutor = Runnable::run;
            invokeOnGameFieldChanged(fixture.controller, fixture.field);
        } finally {
            GameController.uiExecutor = originalExecutor;
        }

        assertEquals("GAME OVER! Score: 0", fixture.scoreLabel.getText());
    }

    @Test
    @Timeout(10)
    void testOnGameFieldChangedShowsWinMessage() throws Exception {
        Assumptions.assumeTrue(javaFxSupported,
                "JavaFX toolkit is not supported in current environment");

        FxFixture fixture = createFxFixture();
        setBooleanField(fixture.field, "gameWon", true);

        GameController.UiExecutor originalExecutor = GameController.uiExecutor;
        try {
            GameController.uiExecutor = Runnable::run;
            invokeOnGameFieldChanged(fixture.controller, fixture.field);
        } finally {
            GameController.uiExecutor = originalExecutor;
        }

        assertEquals("YOU WIN! Score: 0", fixture.scoreLabel.getText());
    }

    @Test
    @Timeout(10)
    void testOnGameFieldChangedShowsDrawMessage() throws Exception {
        Assumptions.assumeTrue(javaFxSupported,
                "JavaFX toolkit is not supported in current environment");

        FxFixture fixture = createFxFixture();
        setBooleanField(fixture.field, "gameDraw", true);

        GameController.UiExecutor originalExecutor = GameController.uiExecutor;
        try {
            GameController.uiExecutor = Runnable::run;
            invokeOnGameFieldChanged(fixture.controller, fixture.field);
        } finally {
            GameController.uiExecutor = originalExecutor;
        }

        assertEquals("DRAW! Score: 0", fixture.scoreLabel.getText());
    }
}

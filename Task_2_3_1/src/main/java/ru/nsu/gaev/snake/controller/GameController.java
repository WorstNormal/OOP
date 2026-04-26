package ru.nsu.gaev.snake.controller;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.gaev.snake.model.common.Direction;
import ru.nsu.gaev.snake.model.common.Level;
import ru.nsu.gaev.snake.model.common.Point;
import ru.nsu.gaev.snake.model.core.GameField;
import ru.nsu.gaev.snake.model.core.GameFieldView;
import ru.nsu.gaev.snake.model.entity.RobotSnake;
import ru.nsu.gaev.snake.model.entity.Snake;
import ru.nsu.gaev.snake.model.strategy.GreedyStrategy;
import ru.nsu.gaev.snake.view.GameRenderer;

/**
 * Контроллер игры, который связывает интерфейс и игровую модель.
 */
public class GameController {
    private static final int CELL_SIZE = 30;
    @FXML
    private Canvas gameCanvas;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label levelLabel;
    private GameField gameField;
    private GameRenderer renderer;
    private AnimationTimer timer;
    private long lastUpdate = 0;

    /**
     * Инициализирует контроллер и создает игровое поле.
     */
    @FXML
    public void initialize() {
        renderer = new GameRenderer(gameCanvas, CELL_SIZE);
        int width = renderer.getFieldWidth();
        int height = renderer.getFieldHeight();
        Level level1 = new Level(1, Integer.MAX_VALUE, 200_000_000L);
        gameField = new GameField(width, height, 5, level1);
        gameField.addListener(this::onGameFieldChanged);
        gameField.addRobot(
                new RobotSnake(
                        new Point(width - 5, height - 5),
                        Direction.LEFT,
                        new GreedyStrategy()
                )
        );
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate
                        >= gameField.getCurrentLevel().tickDurationNs()) {
                    gameField.update();
                    lastUpdate = now;
                }
            }
        };
        updateUI(gameField);
        timer.start();
    }

    private void onGameFieldChanged(GameFieldView field) {
        updateUI(field);
        if (field.isGameOver() || field.isGameWon() || field.isGameDraw()) {
            timer.stop();
            showEndGameMessage(field.isGameWon(), field.isGameDraw());
        }
    }

    /**
     * Обновляет элементы интерфейса.
     */
    private void updateUI(GameFieldView field) {
        renderer.render(field);
        scoreLabel.setText("Score: " + field.getScore());
        levelLabel.setText("Level: " + field.getCurrentLevel().levelNumber());
    }

    /**
     * Показывает итоговое сообщение после завершения игры.
     *
     * @param won true, если игрок победил
     * @param draw true, если игра завершилась вничью
     */
    private void showEndGameMessage(boolean won, boolean draw) {
        Platform.runLater(() -> {
            if (draw) {
                scoreLabel.setText("DRAW! Score: " + gameField.getScore());
            } else if (won) {
                scoreLabel.setText("YOU WIN! Score: " + gameField.getScore());
            } else {
                scoreLabel.setText("GAME OVER! Score: " + gameField.getScore());
            }
        });
    }

    /**
     * Показывает окно с правилами игры.
     */
    @FXML
    public void showRules() {
        boolean wasStarted = gameField != null && gameField.isStarted();
        boolean wasGameOver = gameField != null
                && (gameField.isGameOver() || gameField.isGameWon()
                || gameField.isGameDraw());

        if (wasStarted && !wasGameOver && timer != null) {
            timer.stop();
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Правила игры");
        alert.setHeaderText("Классическая змейка с ИИ-роботами");
        alert.setContentText(
                "1. Управление: W, A, S, D или стрелки.\n"
                        + "2. Цель: Собирать красную еду, расти и набирать "
                        + "очки для победы.\n"
                        + "3. Проигрыш: Столкновение со стеной, со своим "
                        + "хвостом, препятствием или синим ИИ-роботом.\n"
                        + "4. Робот: Синяя змейка (бот) сама ищет кратчайший "
                        + "путь к еде. Будьте осторожны!"
        );
        alert.showAndWait();

        if (wasStarted && !wasGameOver && timer != null) {
            timer.start();
        }

        if (gameCanvas != null && gameCanvas.getScene() != null) {
            gameCanvas.getScene().getRoot().requestFocus();
        }
    }

    /**
     * Обрабатывает нажатия клавиш.
     *
     * @param event событие клавиатуры
     */
    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (!gameField.isStarted() && !gameField.isGameOver()
                && !gameField.isGameWon() && !gameField.isGameDraw()) {
            gameField.setStarted(true);
        }
        KeyCode code = event.getCode();
        Snake player = gameField.getPlayer();
        if (code == KeyCode.W || code == KeyCode.UP) {
            player.setNextDirection(Direction.UP);
        } else if (code == KeyCode.S || code == KeyCode.DOWN) {
            player.setNextDirection(Direction.DOWN);
        } else if (code == KeyCode.A || code == KeyCode.LEFT) {
            player.setNextDirection(Direction.LEFT);
        } else if (code == KeyCode.D || code == KeyCode.RIGHT) {
            player.setNextDirection(Direction.RIGHT);
        }
    }
}

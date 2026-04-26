package ru.nsu.gaev.snake.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Игровое поле, которое хранит состояние матча и применяет правила.
 */
public class GameField {
    private static final Random random = new Random();
    private final int width;
    private final int height;
    private final Snake player;
    private final List<RobotSnake> robots;
    private final List<Food> foods;
    private final List<Obstacle> obstacles;
    private final int numFoods;
    private Level currentLevel;
    private int score = 0;
    private boolean gameWon = false;
    private boolean gameOver = false;
    private boolean gameDraw = false;
    private boolean started = false;

    /**
     * Создает игровое поле.
     *
     * @param width ширина поля
     * @param height высота поля
     * @param numFoods количество еды на старте
     * @param startLevel начальный уровень
     */
    public GameField(int width, int height, int numFoods, Level startLevel) {
        this.width = width;
        this.height = height;
        this.numFoods = numFoods;
        this.currentLevel = startLevel;
        this.player = new Snake(new Point(width / 2, height / 2), Direction.UP);
        this.robots = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.obstacles = new ArrayList<>();
        spawnInitialFoods();
    }

    public void addRobot(RobotSnake robot) {
        robots.add(robot);
    }

    public void addObstacle(Obstacle obs) {
        obstacles.add(obs);
    }

    private void spawnInitialFoods() {
        for (int i = 0; i < numFoods; i++) {
            spawnFood();
        }
    }

    private void spawnFood() {
        if (foods.size() >= width * height / 2) {
            return;
        }
        Point p;
        do {
            p = new Point(random.nextInt(width), random.nextInt(height));
        } while (!isPointFree(p));

        foods.add(new Food(p, FoodType.NORMAL));
    }

    /**
     * Проверяет, свободна ли точка на поле.
     *
     * @param p проверяемая точка
     * @return true, если клетка свободна
     */
    public boolean isPointFree(Point p) {
        if (player.occupies(p)) {
            return false;
        }
        for (RobotSnake r : robots) {
            if (r.occupies(p)) {
                return false;
            }
        }
        for (Food f : foods) {
            if (f.position().equals(p)) {
                return false;
            }
        }
        for (Obstacle o : obstacles) {
            if (o.position().equals(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Выполняет один игровой тик.
     */
    public void update() {
        if (!started || gameOver || gameWon || gameDraw) {
            return;
        }

        if (player.isAlive()) {
            player.move();
        }

        for (RobotSnake robot : robots) {
            if (robot.isAlive()) {
                robot.determineNextMove(this);
                robot.move();
            }
        }

        checkCollisions();
        checkFood();
        checkLevelProgression();
    }

    private void checkCollisions() {
        if (!player.isAlive()) {
            return;
        }

        Point phead = player.getHead();

        if (phead.x() < 0 || phead.x() >= width || phead.y() < 0 || phead.y() >= height) {
            player.kill();
            gameOver = true;
            return;
        }

        if (player.checkSelfCollision()) {
            player.kill();
            gameOver = true;
            return;
        }

        for (Obstacle obs : obstacles) {
            if (phead.equals(obs.position())) {
                player.kill();
                gameOver = true;
                return;
            }
        }

        for (RobotSnake robot : robots) {
            if (!robot.isAlive()) {
                continue;
            }

            if (phead.equals(robot.getHead())) {
                player.kill();
                robot.kill();
                gameDraw = true;
                return;
            }

            if (robot.occupies(phead)) {
                player.kill();
                gameOver = true;
            }

            if (player.occupies(robot.getHead())) {
                robot.kill();
                gameWon = true;
            }
            Point rhead = robot.getHead();
            if (rhead.x() < 0 || rhead.x() >= width || rhead.y() < 0 || rhead.y() >= height) {
                robot.kill();
                gameWon = true;
            }
            if (robot.checkSelfCollision()) {
                robot.kill();
                gameWon = true;
            }
            for (Obstacle obs : obstacles) {
                if (rhead.equals(obs.position())) {
                    robot.kill();
                    gameWon = true;
                }
            }
        }
    }

    private void checkFood() {
        if (player.isAlive()) {
            Point phead = player.getHead();
            Food eaten = null;
            for (Food f : foods) {
                if (f.position().equals(phead)) {
                    player.eat(f);
                    score += f.type().getScoreValue();
                    eaten = f;
                    break;
                }
            }
            if (eaten != null) {
                foods.remove(eaten);
                spawnFood();
            }
        }

        for (RobotSnake robot : robots) {
            if (robot.isAlive()) {
                Point rhead = robot.getHead();
                Food eaten = null;
                for (Food f : foods) {
                    if (f.position().equals(rhead)) {
                        robot.eat(f);
                        eaten = f;
                        break;
                    }
                }
                if (eaten != null) {
                    foods.remove(eaten);
                    spawnFood();
                }
            }
        }
    }

    private void checkLevelProgression() {
        if (score >= currentLevel.targetScore()) {
            gameWon = true;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Snake getPlayer() {
        return player;
    }

    public List<RobotSnake> getRobots() {
        return robots;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Level level) {
        this.currentLevel = level;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameDraw() {
        return gameDraw;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}

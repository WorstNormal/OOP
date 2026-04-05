package ru.nsu.gaev.snake.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {
    private final int width;
    private final int height;
    private final Snake player;
    private final List<RobotSnake> robots;
    private final List<Food> foods;
    private final List<Obstacle> obstacles;
    private Level currentLevel;
    private int score = 0;
    private boolean gameWon = false;
    private boolean gameOver = false;
    private boolean gameDraw = false;
    private boolean started = false;
    private final int numFoods;

    private static final Random random = new Random();

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
        if (foods.size() >= width * height / 2) return; // Prevent infinite loop if too full
        Point p;
        do {
            p = new Point(random.nextInt(width), random.nextInt(height));
        } while (!isPointFree(p));
        
        foods.add(new Food(p, FoodType.NORMAL));
    }

    public boolean isPointFree(Point p) {
        if (player.occupies(p)) return false;
        for (RobotSnake r : robots) {
            if (r.occupies(p)) return false;
        }
        for (Food f : foods) {
            if (f.getPosition().equals(p)) return false;
        }
        for (Obstacle o : obstacles) {
            if (o.getPosition().equals(p)) return false;
        }
        return true;
    }

    public void update() {
        if (!started || gameOver || gameWon || gameDraw) return;

        // Move player
        if (player.isAlive()) {
            player.move();
        }
        
        // Move robots
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
        if (!player.isAlive()) return;
        
        Point pHead = player.getHead();
        
        // Wall collision
        if (pHead.x() < 0 || pHead.x() >= width || pHead.y() < 0 || pHead.y() >= height) {
            player.kill();
            gameOver = true;
            return;
        }

        // Self collision
        if (player.checkSelfCollision()) {
            player.kill();
            gameOver = true;
            return;
        }
        
        // Obstacle collision
        for (Obstacle obs : obstacles) {
            if (pHead.equals(obs.getPosition())) {
                player.kill();
                gameOver = true;
                return;
            }
        }

        // Robot collisions
        for (RobotSnake robot : robots) {
            if (!robot.isAlive()) continue;
            
            // Head to head collision
            if (pHead.equals(robot.getHead())) {
                player.kill();
                robot.kill();
                gameDraw = true;
                return;
            }
            
            // Player hits robot
            if (robot.occupies(pHead)) { // Could be head to head or head to body
                player.kill();
                gameOver = true;
            }
            
            // Robot hits player (tail crossing)
            if (player.occupies(robot.getHead())) {
                robot.kill();
                gameWon = true;
            }
            // Robot wall collision
            Point rHead = robot.getHead();
            if (rHead.x() < 0 || rHead.x() >= width || rHead.y() < 0 || rHead.y() >= height) {
                robot.kill();
                gameWon = true;
            }
            // Robot self collision
            if (robot.checkSelfCollision()) {
                robot.kill();
                gameWon = true;
            }
            // Robot obstacle collision
            for (Obstacle obs : obstacles) {
                if (rHead.equals(obs.getPosition())) {
                    robot.kill();
                    gameWon = true;
                }
            }
        }
    }

    private void checkFood() {
        // Player
        if (player.isAlive()) {
            Point pHead = player.getHead();
            Food eaten = null;
            for (Food f : foods) {
                if (f.getPosition().equals(pHead)) {
                    player.eat(f);
                    score += f.getType().getScoreValue();
                    eaten = f;
                    break;
                }
            }
            if (eaten != null) {
                foods.remove(eaten);
                spawnFood();
            }
        }
        
        // Robots
        for (RobotSnake robot : robots) {
            if (robot.isAlive()) {
                Point rHead = robot.getHead();
                Food eaten = null;
                for (Food f : foods) {
                    if (f.getPosition().equals(rHead)) {
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
        if (score >= currentLevel.getTargetScore()) {
            gameWon = true; 
        }
    }
    
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public Snake getPlayer() { return player; }
    public List<RobotSnake> getRobots() { return robots; }
    public List<Food> getFoods() { return foods; }
    public List<Obstacle> getObstacles() { return obstacles; }
    public Level getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Level level) { this.currentLevel = level; }
    public int getScore() { return score; }
    public boolean isGameOver() { return gameOver; }
    public boolean isGameWon() { return gameWon; }
    public boolean isGameDraw() { return gameDraw; }
    public boolean isStarted() { return started; }
    public void setStarted(boolean started) { this.started = started; }
}

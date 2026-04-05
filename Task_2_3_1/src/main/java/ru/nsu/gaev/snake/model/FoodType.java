package ru.nsu.gaev.snake.model;

public enum FoodType {
    NORMAL(1, 100);

    private final int growthAmount;
    private final int scoreValue;

    FoodType(int growthAmount, int scoreValue) {
        this.growthAmount = growthAmount;
        this.scoreValue = scoreValue;
    }

    public int getGrowthAmount() {
        return growthAmount;
    }

    public int getScoreValue() {
        return scoreValue;
    }
}

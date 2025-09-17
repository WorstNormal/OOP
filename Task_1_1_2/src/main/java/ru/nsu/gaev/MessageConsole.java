package ru.nsu.gaev;

import java.util.List;

/**
 * Класс, отвечающий за вывод сообщений в консоль для игры Blackjack.
 */
public class MessageConsole {
    // Dealer messages
    /**
     * Выводит карты и сумму очков игрока или дилера.
     *
     * @param hand список карт в руке
     * @param score сумма очков
     * @param player имя игрока
     */
    public void cardsMessage(List<String> hand, int score, String player) {
        System.out.println(player + " cards: " + hand + " (sum = " + score + ")");
    }

    /**
     * Выводит сообщение о взятой карте.
     *
     * @param newCard новая карта
     * @param player имя игрока
     */
    public void newCardsMessage(String newCard, String player) {
        System.out.println(player + " drew a card: " + newCard);
    }

    /**
     * Выводит сообщение о переборе очков.
     *
     * @param score итоговый счет
     * @param player имя игрока
     */
    public void bustedMessage(int score, String player) {
        System.out.println(player + " busted! (" + score + ")");
    }

    /**
     * Выводит сообщение о том, что игрок/дилер остановился.
     *
     * @param score итоговый счет
     * @param player имя игрока
     */
    public void standsMessage(int score, String player) {
        System.out.println(player + " stands. Final score: " + score);
    }

    /**
     * Сообщение о том, что у игрока 21 очко.
     */
    public void blackjack() {
        System.out.println("You have 21! You stand automatically.");
    }

    /**
     * Сообщение с выбором действия для игрока.
     */
    public void playerOption() {
        System.out.println("Do you want to hit or stand? ");
    }

    /**
     * Сообщение о том, что не было введено действие.
     */
    public void noInputDetect() {
        System.out.println("No input detected, standing automatically.");
    }

    /**
     * Сообщение о некорректном вводе действия.
     */
    public void errorInputDetect() {
        System.out.println("Please enter 'hit' or 'stand'.");
    }

    /**
     * Приветственное сообщение для игры Blackjack.
     */
    public void welcomeBlackjack() {
        System.out.println("Welcome to Blackjack!");
    }

    /**
     * Сообщение о номере раунда.
     *
     * @param roundCounter номер раунда
     */
    public void amountRound(Integer roundCounter) {
        System.out.println("\n=== Round " + roundCounter + " ===");
    }

    /**
     * Сообщение с предложением сыграть ещё раз.
     */
    public void playAgain() {
        System.out.print("Play again? (yes/no): ");
    }

    /**
     * Сообщение с просьбой ввести yes или no.
     */
    public void yesOrNo() {
        System.out.print("Please enter 'yes' or 'no': ");
    }

    /**
     * Сообщение о завершении игры.
     *
     * @param roundCounter количество сыгранных раундов
     */
    public void gameOver(Integer roundCounter) {
        System.out.println("Game over. You played " + roundCounter + " rounds.");
    }

    /**
     * Сообщение с просьбой ввести количество колод.
     */
    public void numberOfDecks() {
        System.out.print("Enter number of decks to use (at least 1): ");
    }

    /**
     * Сообщение о выигрыше игрока с блекджеком.
     */
    public void playerBlackjack() {
        System.out.println("Blackjack! Player wins immediately!");
    }

    /**
     * Сообщение о победе игрока или дилера.
     *
     * @param player имя победителя
     */
    public void win(String player) {
        System.out.println(player + " wins!");
    }

    /**
     * Сообщение о ничьей.
     */
    public void tie() {
        System.out.println("Push (tie)!");
    }
}

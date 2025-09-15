package ru.nsu.gaev;

import java.util.Scanner;

public class GameLogic {
    static Scanner scanner = new Scanner(System.in);
    private static int roundCounter = 0;

    /**
     * Основной метод игры. Запускает раунды игры Blackjack до тех пор,
     * пока игрок не выберет завершение.
     */
    public static void main() {
        System.out.println("Welcome to Blackjack!");

        while (true) {
            roundCounter++;
            System.out.println("\n=== Round " + roundCounter + " ===");
            startRound();

            // спрашиваем про продолжение
            System.out.print("Play again? (yes/no): ");
            String answer = scanner.next().toLowerCase();
            while (!answer.equals("yes") && !answer.equals("no")) {
                System.out.print("Please enter 'yes' or 'no': ");
                answer = scanner.next().toLowerCase();
            }
            if (answer.equals("no")) {
                System.out.println("Game over. You played " + roundCounter +
                        " rounds.");
                break;
            }
        }
    }

    /**
     * Запускает один раунд игры Blackjack. Запрашивает количество колод карт, инициализирует игрока и дилера,
     * обрабатывает ход игрока, проверяет его баллы, после чего ходит дилер и определяется победитель.
     */
    public static void startRound() {
        System.out.print("Enter number of decks to use (at least 1): ");
        int countDecks = scanner.nextInt();
        while (countDecks < 1) {
            System.out.print("Please enter a number >= 1: ");
            countDecks = scanner.nextInt();
        }

        Deck deck = new Deck(countDecks);

        // инициализация игрока
        PlayerLogic player = new PlayerLogic(deck);
        player.addCard(deck.drawCard());
        player.addCard(deck.drawCard());

        // инициализация дилера
        DealerLogic dealer = new DealerLogic(deck);
        dealer.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());

        System.out.println("Player's hand: " + player.getHand() +
                " (total = " + player.getScore() + ")");
        System.out.println("Dealer's hand: [" + dealer.getVisibleCard() +
                ", X]");

        // ход игрока
        player.playerTurn();

        // проверка на блекджек у игрока
        if (player.getScore() == 21) {
            System.out.println("Blackjack! Player wins immediately!");
            return; // раунд закончен
        }

        // ход дилера, если игрок не перебрал
        if (player.getScore() <= 21) {
            dealer.dealerTurn();

            // сравнение результатов
            int playerScore = player.getScore();
            int dealerScore = dealer.getScore();

            if (dealerScore > 21 || playerScore > dealerScore) {
                System.out.println("Player wins!");
            } else if (dealerScore > playerScore) {
                System.out.println("Dealer wins!");
            } else {
                System.out.println("Push (tie)!");
            }
        }
    }
}

package ru.nsu.gaev;

import java.util.Scanner;


public class GameLogic {
    public static final Scanner scanner = new Scanner(System.in);
    private static int roundCounter = 0;
    public static MessageConsole message = new MessageConsole();

    /**
     * Основной метод игры. Запускает раунды игры Blackjack до тех пор,
     * пока игрок не выберет завершение.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        message.welcomeBlackjack();
        while (true) {
            roundCounter++;
            message.amountRound(roundCounter);
            startRound(scanner);

            message.playAgain();
            String answer = scanner.nextLine().toLowerCase();
            while (!answer.equals("yes") && !answer.equals("no")) {
                message.yesOrNo();
                answer = scanner.nextLine().toLowerCase();
            }
            if (answer.equals("no")) {
                message.gameOver(roundCounter);
                break;
            }
        }
    }

    /**
     * Запускает один раунд игры Blackjack. Запрашивает количество колод карт, инициализирует игрока и дилера,
     * обрабатывает ход игрока, проверяет его баллы, после чего ходит дилер и определяется победитель.
     */
    public static void startRound(Scanner scanner) {
        message.numberOfDecks();
        int countDecks = scanner.nextInt();
        scanner.nextLine(); // чтобы "съесть" перевод строки

        Deck deck = new Deck(countDecks);

        PlayerLogic player = new PlayerLogic(deck);
        player.addCard(deck.drawCard());
        player.addCard(deck.drawCard());

        DealerLogic dealer = new DealerLogic(deck);
        dealer.addCard(deck.drawCard());
        dealer.addCard(deck.drawCard());

        message.cardsMessage(dealer.getVisibleCard(), dealer.getScore(),"Dilear");

        player.playerTurn(scanner);

        // проверка на блекджек у игрока
        if (player.getScore() == 21) {
            message.playerBlackjack();
            return; // раунд закончен
        }

        // ход дилера, если игрок не перебрал
        if (player.getScore() <= 21) {
            dealer.dealerTurn();

            // сравнение результатов
            int playerScore = player.getScore();
            int dealerScore = dealer.getScore();

            if (dealerScore > 21 || playerScore > dealerScore) {
                message.win("Player");
            } else if (dealerScore > playerScore) {
                message.win("Dialer");
            } else {
                message.tie();
            }
        }
    }
}

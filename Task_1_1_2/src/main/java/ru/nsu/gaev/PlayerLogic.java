package ru.nsu.gaev;

import java.util.Scanner;

/**
 * Класс, реализующий логику игрока в игре Blackjack.
 */
public class PlayerLogic extends Participant {
    public PlayerLogic(Deck deck) {
        super(deck);
    }

    /**
     * Логика хода игрока: позволяет брать карты или остановиться.
     *
     * @param scanner сканер для ввода пользователя
     */
    public void playerTurn(Scanner scanner) {
        message.cardsMessage(hand, getScore(), "Player");

        while (true) {
            if (getScore() > 21) {
                message.bustedMessage(getScore(), "Player");
                break;
            }

            if (getScore() == 21) {
                message.blackjack();
                break;
            }

            message.playerOption();
            if (!scanner.hasNextLine()) {
                message.noInputDetect();
                break;
            }
            String action = scanner.nextLine().toLowerCase();

            if (action.equals("hit")) {
                String newCard = deck.drawCard();
                addCard(newCard);
                message.newCardsMessage(newCard, "Player");
                message.cardsMessage(hand, getScore(), "Player");
            } else if (action.equals("stand")) {
                message.standsMessage(getScore(), "Player");
                break;
            } else {
                message.errorInputDetect();
            }
        }
    }
}

package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Класс, реализующий логику игрока в игре Blackjack.
 */
public class PlayerLogic {
    private final List<String> hand = new ArrayList<>();
    private final Deck deck;
    public static MessageConsole message = new MessageConsole();

    /**
     * Конструктор для инициализации игрока с определённой колодой.
     *
     * @param deck колода карт, из которой будут раздавать карты.
     */
    public PlayerLogic(Deck deck) {
        this.deck = deck;
    }

    /**
     * Возвращает количество карт в руке игрока.
     *
     * @return количество карт в руке
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Добавляет карту в руку игрока.
     *
     * @param card карта в виде строки, которая добавляется в руку игрока (например, "2H", "KS").
     */
    public void addCard(String card) {
        hand.add(card);
    }

    /**
     * Возвращает текущий счёт игрока. Счёт рассчитывается как сумма значений карт в руке.
     *
     * @return текущий счёт игрока
     */
    public int getScore() {
        return Deck.calculateHandValue(hand);
    }

    /**
     * Возвращает копию руки игрока.
     *
     * @return список карт в руке
     */
    public List<String> getHand() {
        return new ArrayList<>(hand);
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

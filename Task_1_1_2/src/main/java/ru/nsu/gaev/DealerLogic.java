package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий логику дилера в игре Blackjack.
 */
public class DealerLogic extends Participant {
    /**
     * Конструктор для инициализации дилера.
     * Инициализирует колоду, из которой будут вытягиваться карты для дилера.
     *
     * @param deck колода карт, из которой будут раздавать карты.
     */
    public DealerLogic(Deck deck) {
        super(deck);
    }

    /**
     * Возвращает видимую карту дилера (первая в руке) как список для совместимости с выводом.
     */
    public ArrayList<String> getVisibleCard() {
        if (hand.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(List.of(hand.get(0)));
    }

    /**
     * Ход дилера по правилам игры. Дилер продолжает брать карты,
     * пока не наберет минимум 17 очков.
     */
    public void dealerTurn() {
        message.cardsMessage(hand, getScore(), "Dealer");

        while (getScore() < 17) {
            String newCard = deck.drawCard();
            addCard(newCard);
            message.newCardsMessage(newCard, "Dealer");
            message.cardsMessage(hand, getScore(), "Dealer");
        }

        if (getScore() > 21) {
            message.bustedMessage(getScore(), "Dealer");
        } else {
            message.standsMessage(getScore(), "Dealer");
        }
    }
}

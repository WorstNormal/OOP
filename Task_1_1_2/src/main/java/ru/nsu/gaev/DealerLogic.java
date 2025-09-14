package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;

public class DealerLogic {
    private final List<String> hand = new ArrayList<>();
    private final Deck deck;

    public DealerLogic(Deck deck) {
        this.deck = deck;
    }

    public void addCard(String card) {
        hand.add(card);
    }

    public int getScore() {
        return Deck.calculateHandValue(hand);
    }

    public List<String> getHand() {
        return new ArrayList<>(hand);
    }

    public String getVisibleCard() {
        return hand.get(0);
    }

    // ход дилера по правилам
    public void dealerTurn() {
        System.out.println("Карты дилера: " + hand + " (сумма = " + getScore() + ")");
        while (getScore() < 17) {
            String newCard = deck.drawCard();
            addCard(newCard);
            System.out.println("Дилер взял карту: " + newCard);
            System.out.println("Карты дилера: " + hand + " (сумма = " + getScore() + ")");
        }

        if (getScore() > 21) {
            System.out.println("У дилера перебор! (" + getScore() + ")");
        } else {
            System.out.println("Дилер остановился. Итоговое количество очков: " + getScore());
        }
    }
}

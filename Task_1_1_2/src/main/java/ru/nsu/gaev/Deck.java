package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final ArrayList<String> cards = new ArrayList<>();

    public Deck(int countDecks) {
        String[] suits = {"S", "H", "D", "C"};
        String[] ranks = {
                "2","3","4","5","6","7","8","9","10",
                "J","Q","K","A"
        };

        for (int i = 0; i < countDecks; i++) {
            for (String suit : suits) {
                for (String rank : ranks) {
                    cards.add(rank + suit);
                }
            }
        }

        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public String drawCard() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Колода пустая!");
        }
        return cards.remove(0);
    }

    // значение карты (туз = 11 по умолчанию)
    private static int getCardValue(String card) {
        String rank = card.substring(0, card.length() - 1);
        return switch (rank) {
            case "J", "Q", "K" -> 10;
            case "A" -> 11;
            default -> Integer.parseInt(rank);
        };
    }

    // сумма руки с учётом тузов
    public static int calculateHandValue(List<String> hand) {
        int total = 0;
        int aces = 0;

        for (String card : hand) {
            int value = getCardValue(card);
            total += value;
            if (card.startsWith("A")) {
                aces++;
            }
        }

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        return total;
    }
}

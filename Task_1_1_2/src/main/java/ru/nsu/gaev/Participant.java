package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;

/**
 * Базовый абстрактный класс для участников игры (игрок и дилер).
 * Содержит общие поля и поведение: рука, колода и простые операции над рукой.
 */
public abstract class Participant {
    protected final List<String> hand = new ArrayList<>();
    protected final Deck deck;
    protected final MessageConsole message = new MessageConsole();

    protected Participant(Deck deck) {
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

    public int getHandSize() {
        return hand.size();
    }
}


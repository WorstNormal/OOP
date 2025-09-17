package ru.nsu.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Тесты для класса Deck (колода карт для игры Blackjack).
 */
class DeckTest {
    private Deck deck1;

    @BeforeEach
    void deck1create() {
        deck1 = new Deck(1);
    }

    /**
     * Проверяет, что две новые колоды отличаются (перетасованы).
     */
    @Test
    void shuffleTest()
    {
        Deck deck2 = new Deck(1);
        assertNotEquals(deck1, deck2);
    }

    /**
     * Проверяет, что количество карт в двух новых колодах совпадает.
     */
    @Test
    void getCardsCountTest()
    {
        Deck deck2 = new Deck(1);
        assertEquals(deck1.getCardsCount(), deck2.getCardsCount());
    }

    /**
     * Проверяет уменьшение количества карт после вытягивания карты.
     */
    @Test
    void drawTest()
    {
        Deck deck2 = new Deck(1);
        deck2.drawCard();
        assertNotEquals(deck1.getCardsCount(), deck2.getCardsCount());
    }

    /**
     * Проверяет корректность определения значения карты.
     */
    @Test
    void getCardValueTest()
    {
        int value_A = Deck.getCardValue("AH");
        assertEquals(11, value_A);
        int value_J = Deck.getCardValue("JH");
        assertEquals(10, value_J);
        int value_6 = Deck.getCardValue("6H");
        assertEquals(6, value_6);
    }

    /**
     * Проверяет корректность подсчёта суммы очков для различных комбинаций карт.
     */
    @Test
    void calculateHandValueTest()
    {
        int value_A_10_10 = Deck.calculateHandValue(java.util.List.of("AH", "10H", "10S"));
        assertEquals(21, value_A_10_10);
        int value_A_A_A = Deck.calculateHandValue(java.util.List.of("AH", "AC", "AS"));
        assertEquals(13, value_A_A_A);
        int value_J_K_Q = Deck.calculateHandValue(java.util.List.of("JH", "KC", "QS"));
        assertEquals(30, value_J_K_Q);
    }
}

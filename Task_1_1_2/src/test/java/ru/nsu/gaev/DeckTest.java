package ru.nsu.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck(1);
    }

    @Test
    void testDeckInitialization() {
        // 52 карты в одной колоде
        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.drawCard());
        }

        // После 52-х карт — должно выброситься исключение
        assertThrows(IllegalStateException.class, deck::drawCard);
    }

    @Test
    void testShuffle() {
        // Проверка на то, что после shuffle порядок не предсказуем
        Deck deck1 = new Deck(1);
        Deck deck2 = new Deck(1);
        assertNotEquals(deck1.drawCard(), deck2.drawCard());
    }

    @Test
    void testCalculateHandValueWithoutAce() {
        List<String> hand = Arrays.asList("10H", "7S");
        assertEquals(17, Deck.calculateHandValue(hand));
    }

    @Test
    void testCalculateHandValueWithOneAce() {
        List<String> hand = Arrays.asList("AH", "7S");
        assertEquals(18, Deck.calculateHandValue(hand));
    }

    @Test
    void testCalculateHandValueWithMultipleAces() {
        List<String> hand = Arrays.asList("AH", "AD", "8C");
        assertEquals(20, Deck.calculateHandValue(hand));
    }

    @Test
    void testCalculateHandValueBustingWithAces() {
        List<String> hand = Arrays.asList("AH", "AD", "AC", "9S");
        assertEquals(12, Deck.calculateHandValue(hand));
    }
}

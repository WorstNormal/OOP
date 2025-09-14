package ru.nsu.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DealerLogicTest {
    private DealerLogic dealer;
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck(1);
        dealer = new DealerLogic(deck);
    }

    @Test
    void testAddCardAndGetHand() {
        dealer.addCard("10H");
        dealer.addCard("AS");
        List<String> hand = dealer.getHand();
        assertEquals(2, hand.size());
        assertTrue(hand.contains("10H"));
        assertTrue(hand.contains("AS"));
    }

    @Test
    void testGetScore() {
        dealer.addCard("10H");
        dealer.addCard("AS");
        int score = dealer.getScore();
        assertEquals(21, score);
    }
}

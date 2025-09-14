package ru.nsu.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerLogicTest {
    private PlayerLogic player;
    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck(1);
        player = new PlayerLogic(deck);
    }

    @Test
    void testAddCardAndGetHand() {
        player.addCard("10D");
        player.addCard("7S");

        List<String> hand = player.getHand();
        assertEquals(2, hand.size());
        assertTrue(hand.contains("10D"));
        assertTrue(hand.contains("7S"));
    }

    @Test
    void testGetScore() {
        player.addCard("10D");
        player.addCard("AS");

        assertEquals(21, player.getScore());
    }
}

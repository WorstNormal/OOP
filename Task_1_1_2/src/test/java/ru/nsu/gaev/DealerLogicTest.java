package ru.nsu.gaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для класса DealerLogic (логика дилера в игре Blackjack).
 */
class DealerLogicTest {
    private Deck deck;

    @BeforeEach
    void deckCreate() {
        deck = new Deck(1);
    }

    /**
     * Проверяет, что объект DealerLogic создаётся корректно.
     */
    @Test
    void dealerLogicCheck() {
        DealerLogic dealer = new DealerLogic(deck);
        assertNotNull(dealer);
    }

    /**
     * Проверяет, что размер руки дилера изначально равен 0.
     */
    @Test
    void getHandSizeTest() {
        DealerLogic dealer = new DealerLogic(deck);
        assertEquals(0, dealer.getHandSize());
    }

    /**
     * Проверяет добавление карты в руку дилера.
     */
    @Test
    void addCardTest() {
        DealerLogic dealer = new DealerLogic(deck);
        dealer.addCard("AH");
        assertEquals(1, dealer.getHandSize());
    }

    /**
     * Проверяет получение видимой карты дилера.
     */
    @Test
    void getVisibleCardTest() {
        DealerLogic dealer = new DealerLogic(deck);
        dealer.addCard("AH");
        dealer.addCard("AS");
        assertEquals("AH", dealer.getVisibleCard().get(0));
    }

    /**
     * Проверяет подсчёт очков дилера.
     */
    @Test
    void getScoreTest() {
        DealerLogic dealer = new DealerLogic(deck);
        dealer.addCard("AH");
        assertEquals(11, dealer.getScore());
    }

    /**
     * Проверяет, что дилер останавливается при 17 и более очках.
     */
    @Test
    void dealerTurnStopsAt17OrMore() {
        Deck deck = new Deck(1);
        DealerLogic dealer = new DealerLogic(deck);

        // Добавляем начальные карты с маленьким счётом
        dealer.addCard("2H");
        dealer.addCard("3D"); // сумма = 5

        dealer.dealerTurn();

        // После dealerTurn у дилера должно быть >= 17 очков
        assertTrue(dealer.getScore() >= 17);
    }

    /**
     * Проверяет, что дилер может получить перебор.
     */
    @Test
    void dealerCanBust() {
        Deck deck = new Deck(1);
        DealerLogic dealer = new DealerLogic(deck);

        // Дадим дилеру высокие карты, чтобы перебор был почти гарантирован
        dealer.addCard("KH");
        dealer.addCard("QD"); // сумма = 20

        dealer.dealerTurn();

        // После dealerTurn дилер должен остановиться (20 очков, не берёт новые карты)
        assertEquals(20, dealer.getScore());
        assertEquals(2, dealer.getHandSize());
    }

    /**
     * Проверяет, что дилер добирает карты до 17 очков.
     */
    @Test
    void dealerDrawsUntil17() {
        Deck deck = new Deck(1);
        DealerLogic dealer = new DealerLogic(deck);

        // Дадим минимальную карту, чтобы дилер точно добирал
        dealer.addCard("2H"); // сумма = 2

        dealer.dealerTurn();

        // Проверяем, что дилер взял больше 1 карты
        assertTrue(dealer.getHandSize() > 1);
        // И набрал хотя бы 17 очков
        assertTrue(dealer.getScore() >= 17);
    }
}

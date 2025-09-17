package ru.nsu.gaev;

import java.util.List;

/**
 * Мок-колода для тестирования логики игрока (PlayerLogic).
 */
public class MockDeck extends Deck {
    private final List<String> mockCards;
    private int currentCardIndex = 0;

    /**
     * Конструктор мок-колоды.
     *
     * @param mockCards список карт, которые будут возвращаться при вызове drawCard()
     */
    public MockDeck(List<String> mockCards) {
        super(0);  // Пустая колода
        this.mockCards = mockCards;
    }

    /**
     * Возвращает карту из списка mockCards по порядку.
     *
     * @return карта из списка или пустая строка, если карты закончились
     */
    @Override
    public String drawCard() {
        if (currentCardIndex < mockCards.size()) {
            return mockCards.get(currentCardIndex++);
        }
        return "";
    }
}


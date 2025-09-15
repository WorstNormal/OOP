package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;

public class DealerLogic {
    private final List<String> hand = new ArrayList<>();
    private final Deck deck;

    /**
     * Конструктор для инициализации дилера. Инициализирует колоду, из которой будут вытягиваться карты для дилера.
     *
     * @param deck колода карт, из которой будут раздавать карты.
     */
    public DealerLogic(Deck deck) {
        this.deck = deck;
    }

    /**
     * Добавляет карту в руку дилера.
     *
     * @param card карта в виде строки, которая добавляется в руку дилера (например, "2H", "KS").
     */
    public void addCard(String card) {
        hand.add(card);
    }

    /**
     * Возвращает текущий счёт дилера. Счёт рассчитывается как сумма значений карт в руке.
     *
     * @return текущий счёт дилера.
     */
    public int getScore() {
        return Deck.calculateHandValue(hand);
    }

    /**
     * Возвращает количество карт у дилера.
     * Метод не изменяет содержимое руки, а только
     * сообщает текущее количество элементов в списке {@code cards}.
     *
     * @return число карт, которые сейчас находятся в руке
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Возвращает карту, которая видна игроку (первая карта в руке дилера).
     *
     * @return строка, представляющая видимую карту дилера (например, "2H").
     */
    public String getVisibleCard() {
        return hand.get(0);
    }

    /**
     * Ход дилера по правилам игры. Дилер продолжает брать карты, пока не наберет минимум 17 очков.
     * Если у дилера перебор, выводится соответствующее сообщение.
     * После завершения хода выводится итоговое количество очков дилера.
     */
    public void dealerTurn() {
        System.out.println("Dealer's cards: " + hand + " (sum = " + getScore() + ")");

        // Пока у дилера сумма очков меньше 17, он берет карту
        while (getScore() < 17) {
            String newCard = deck.drawCard();
            addCard(newCard);
            System.out.println("Dealer drew a card: " + newCard);
            System.out.println("Dealer's cards: " + hand + " (sum = " + getScore() + ")");
        }

        // Если у дилера перебор, выводим сообщение о переборе
        if (getScore() > 21) {
            System.out.println("Dealer busted! (" + getScore() + ")");
        } else {
            // Если у дилера не перебор, выводим итоговый счёт
            System.out.println("Dealer stands. Final score: " + getScore());
        }
    }
}

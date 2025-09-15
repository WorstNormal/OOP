package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerLogic {
    private final List<String> hand = new ArrayList<>();
    private final Deck deck;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Конструктор для инициализации игрока. Инициализирует колоду, из которой будут вытягиваться карты для игрока.
     *
     * @param deck колода карт, из которой будут раздавать карты.
     */
    public PlayerLogic(Deck deck) {
        this.deck = deck;
    }

    /**
     * Возвращает количество карт у игрока.
     * Метод не изменяет содержимое руки, а только
     * сообщает текущее количество элементов в списке {@code cards}.
     *
     * @return число карт, которые сейчас находятся в руке
     */
    public int getHandSize() {
        return hand.size();
    }

    /**
     * Добавляет карту в руку игрока.
     *
     * @param card карта в виде строки, которая добавляется в руку игрока (например, "2H", "KS").
     */
    public void addCard(String card) {
        hand.add(card);
    }

    /**
     * Возвращает текущий счёт игрока. Счёт рассчитывается как сумма значений карт в руке.
     *
     * @return текущий счёт игрока.
     */
    public int getScore() {
        return Deck.calculateHandValue(hand);
    }

    /**
     * Возвращает список карт в руке игрока.
     *
     * @return список карт в руке игрока.
     */
    public List<String> getHand() {
        return new ArrayList<>(hand);
    }

    /**
     * Ход игрока. Игрок может выбрать, взять ли карту (hit) или остановиться (stand).
     * Ход продолжается до тех пор, пока игрок не решит остановиться или не переберёт.
     * При переборе или получении 21 игрок автоматически заканчивает ход.
     */
    public void playerTurn() {
        System.out.println("Your cards: " + hand + " (total = " + getScore() + ")");

        while (true) {
            // Проверка, не перебрал ли игрок
            if (getScore() > 21) {
                System.out.println("Bust! You have " + getScore() + " points.");
                break;
            }

            // Если у игрока 21 очко, он автоматически останавливается
            if (getScore() == 21) {
                System.out.println("You have 21! You stand automatically.");
                break;
            }

            // Запрос действия у игрока (взять карту или остановиться)
            System.out.println("Do you want to hit or stand? ");
            String action = scanner.nextLine().toLowerCase();


            // Действие "hit" - игрок берет карту
            if (action.equals("hit")) {
                String newCard = deck.drawCard();
                addCard(newCard);
                System.out.println("You drew: " + newCard);
                System.out.println("Your cards: " + hand + " (total = " + getScore() + ")");

                // Действие "stand" - игрок останавливается
            } else if (action.equals("stand")) {
                System.out.println("You stand. Final score: " + getScore());
                break;
            } else {
                // Если введено что-то некорректное, игроку нужно ввести правильный выбор
                System.out.println("Please enter 'hit' or 'stand'.");
            }
        }
    }
}

package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс, реализующий колоду карт для игры Blackjack.
 */
public class Deck {
    private final ArrayList<String> cards = new ArrayList<>();

    /**
     * Конструктор колоды. Заполняет колоду карт в зависимости от количества колод.
     * Каждая карта представлена как строка, состоящая из ранга и масти.
     * После инициализации колода перетасовывается.
     *
     * @param countDecks количество колод, которые будут использованы в игре.
     */
    public Deck(int countDecks) {
        String[] suits = {"S", "H", "D", "C"};  // Масти карт
        String[] ranks = {
                "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "J", "Q", "K", "A"
        };  // Ранги карт

        for (int i = 0; i < countDecks; i++) {
            for (String suit : suits) {
                for (String rank : ranks) {
                    cards.add(rank + suit);  // Формируем карты в виде строки: "2H", "KS" и т. д.
                }
            }
        }
        Collections.shuffle(cards); // Перетасовываем колоду после её создания
    }

    /**
     * Вытягивает одну карту из колоды.
     * Если колода пуста, выбрасывается исключение.
     *
     * @return строка, представляющая карту, которая была вытянута (например, "2H").
     * @throws IllegalStateException если колода пуста.
     */
    public String drawCard() {
        return cards.remove(0);  // Удаляем и возвращаем первую карту из колоды
    }

    /**
     * Возвращает количество оставшихся карт в колоде.
     * Метод не изменяет содержимое колоды, а только
     * сообщает текущее количество элементов в списке {@code cards}.
     *
     * @return число карт, которые сейчас находятся в колоде
     */
    public int getCardsCount() {
        return cards.size();
    }

    /**
     * Возвращает значение карты. Тузы по умолчанию имеют значение 11,
     * а карты с изображением (J, Q, K) оцениваются в 10 очков.
     *
     * @param card карта в виде строки (например, "2H" или "KH").
     * @return значение карты (например, 2, 10, 11).
     */
    public static int getCardValue(String card) {
        String rank = card.substring(0, card.length() - 1);  // Извлекаем ранг карты
        return switch (rank) {
            case "J", "Q", "K" -> 10;  // Карты с изображением имеют значение 10
            case "A" -> 11;  // Туз имеет значение 11
            default -> Integer.parseInt(rank);  // Для всех других карт возвращаем их числовое значение
        };
    }

    /**
     * Рассчитывает сумму значений карт в руке с учетом тузов.
     * Если сумма превышает 21 и есть туз, его значение будет уменьшено с 11 до 1, чтобы избежать перебора.
     *
     * @param hand список карт в руке игрока (каждая карта представлена строкой).
     * @return итоговая сумма значений карт в руке.
     */
    public static int calculateHandValue(List<String> hand) {
        int total = 0;  // Сумма карт
        int aces = 0;  // Количество тузов

        // Проходим по картам в руке
        for (String card : hand) {
            int value = getCardValue(card);  // Получаем значение карты
            total += value;
            if (card.startsWith("A")) {  // Если карта - туз
                aces++;
            }
        }

        // Если сумма больше 21 и есть тузы, уменьшаем их значение с 11 до 1
        while (total > 21 && aces > 0) {
            total -= 10;  // Уменьшаем сумму на 10 (превращаем туз из 11 в 1)
            aces--;
        }

        return total;  // Возвращаем итоговую сумму
    }
}

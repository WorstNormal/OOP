package ru.nsu.gaev;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.nsu.gaev.GameLogic.scanner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса PlayerLogic (логика игрока в игре Blackjack).
 */
class PlayerLogicTest {
    private PlayerLogic player;

    // Поля для перехвата вывода
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void deckCreate() {
        Deck deck = new Deck(1);
        player = new PlayerLogic(deck);
        // Перехват вывода
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        // Перехват ввода
        originalIn = System.in;
    }

    /**
     * Проверяет, что объект PlayerLogic создаётся корректно.
     */
    @Test
    void playerLogicCheck() {
        assertNotNull(player);
    }

    /**
     * Проверяет, что размер руки игрока изначально равен 0.
     */
    @Test
    void getHandSizeTest() {
        assertEquals(0, player.getHandSize());
    }

    /**
     * Проверяет добавление карты в руку игрока.
     */
    @Test
    void addCardTest() {
        player.addCard("AH");
        assertEquals(1, player.getHandSize());
    }

    /**
     * Проверяет корректность подсчёта очков для одной карты.
     */
    @Test
    void getScoreTest() {
        player.addCard("AH");
        assertEquals(11, player.getScore());
    }

    /**
     * Проверяет получение списка карт в руке игрока.
     */
    @Test
    void getHandTest() {
        player.addCard("AH");
        player.addCard("AS");
        assertArrayEquals(new String[]{"AH", "AS"}, player.getHand().toArray(new String[0]));
    }

    /**
     * Проверяет сценарий, когда игрок добирает карты и получает 21 очко (туз + король).
     */
    @Test
    void playerTurnTestAce() {
        String input = "hit\nstand\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        player.addCard("AH");
        player.addCard("KH");

        Scanner testScanner = new Scanner(new ByteArrayInputStream("hit\nstand\n".getBytes()));
        player.playerTurn(testScanner);
        String output = outputStream.toString();
        assertTrue(output.contains("Player cards:"));
        assertTrue(output.contains("You have 21! You stand automatically."));
    }

    /**
     * Проверяет сценарий, когда у игрока перебор очков.
     */
    @Test
    void playerTurnTestBust() {
        String input = "stand\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        player.addCard("JH");
        player.addCard("KS");
        player.addCard("KH");
        player.playerTurn(scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Player cards: "));
        assertTrue(output.contains("busted!"));
    }

    /**
     * Проверяет обработку некорректного ввода действия игрока.
     */
    @Test
    void playerTurnTestMistake() {
        String input = "std\nstand\n"; // первое введено некорректно, второе — правильно
        Scanner testScanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        player.addCard("4D");
        player.addCard("4S");
        player.playerTurn(testScanner);

        String output = outputStream.toString();
        // Проверяем, что вывод содержит сообщение о неверном вводе
        assertTrue(output.contains("Please enter 'hit' or 'stand'"));
    }

    /**
     * Проверяет корректность добавления карт при выборе действия "hit".
     */
    @Test
    void playerTurnTestHit() {
        String input = "hit\nhit\nstand\n";
        Scanner testScanner = new Scanner(new ByteArrayInputStream(input.getBytes()));

        MockDeck mockDeck = new MockDeck(Arrays.asList("4H", "2D"));
        PlayerLogic testPlayer = new PlayerLogic(mockDeck);
        testPlayer.addCard("4D");
        testPlayer.addCard("4S");

        testPlayer.playerTurn(testScanner);
        String output = outputStream.toString();

        assertTrue(output.contains("Player cards: [4D, 4S, 4H, 2D]"));
    }

    /**
     * Восстанавливает стандартные потоки ввода/вывода после каждого теста.
     */
    @AfterEach
    void restoreSystemStreams() {
        System.setOut(originalOut);  // Восстановить стандартный поток вывода
        System.setIn(originalIn);  // Восстановить стандартный поток ввода
    }
}

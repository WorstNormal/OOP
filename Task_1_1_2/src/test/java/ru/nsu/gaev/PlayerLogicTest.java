package ru.nsu.gaev;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.nsu.gaev.GameLogic.scanner;

class PlayerLogicTest {
    private Deck deck;
    private Deck fackdeck;

    private PlayerLogic player;
    private PlayerLogic facePlayer;


    // Поля для перехвата вывода
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private InputStream originalIn;

    @BeforeEach
    void deckCreate() {
        deck = new Deck(1);
        player = new PlayerLogic(deck);

        fackdeck = new Deck(0);
        facePlayer = new PlayerLogic(fackdeck);

        // Перехват вывода
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        // Перехват ввода
        originalIn = System.in;
    }

    @Test
    void PlayerLogicCheck() {
        assertNotNull(player);
    }

    @Test
    void getHandSizeTest() {
        assertEquals(0, player.getHandSize());
    }

    @Test
    void addCardTest() {
        player.addCard("AH");
        assertEquals(1, player.getHandSize());
    }

    @Test
    void getScoreTest() {
        player.addCard("AH");
        assertEquals(11, player.getScore());  // Предположительно, "AH" = 11 очков
    }

    @Test
    void getHandTest() {
        player.addCard("AH");
        player.addCard("AS");
        assertArrayEquals(new String[]{"AH", "AS"}, player.getHand().toArray(new String[0]));
    }

    @Test
    void playerTurnTestAce() {
        String input = "hit\nstand\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        player.addCard("AH");
        player.addCard("KH");

        Scanner testScanner = new Scanner(new ByteArrayInputStream("hit\nstand\n".getBytes()));
        player.playerTurn(testScanner);
        String output = outputStream.toString();
        assertTrue(output.contains("Player cards:"));
        assertTrue(output.contains("You have 21! You stand automatically."));
    }

    @Test
    void playerTurnTestBust() {
        String input = "stand\n";
        System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
        player.addCard("JH");
        player.addCard("KS");
        player.addCard("KH");
        player.playerTurn(scanner);

        String output = outputStream.toString();
        assertTrue(output.contains("Player cards: "));
        assertTrue(output.contains("busted!"));
    }


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



    @AfterEach
    void restoreSystemStreams() {
        System.setOut(originalOut);  // Восстановить стандартный поток вывода
        System.setIn(originalIn);  // Восстановить стандартный поток ввода
    }
}
// Мок-колода для тестирования
class MockDeck extends Deck {
    private final List<String> mockCards;
    private int currentCardIndex = 0;

    public MockDeck(List<String> mockCards) {
        super(0);  // Пустая колода
        this.mockCards = mockCards;
    }

    @Override
    public String drawCard() {
        // Возвращаем карты из списка по порядку
        if (currentCardIndex < mockCards.size()) {
            return mockCards.get(currentCardIndex++);
        }
        return "";  // Если карты закончились, возвращаем пустую строку (или можно обработать ошибку)
    }
}
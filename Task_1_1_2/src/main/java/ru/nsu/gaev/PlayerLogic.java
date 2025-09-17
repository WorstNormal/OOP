package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerLogic {
    private final List<String> hand = new ArrayList<>();
    private final Deck deck;
    public static MessageConsole message = new MessageConsole();

    public PlayerLogic(Deck deck) {
        this.deck = deck;
    }

    public int getHandSize() {
        return hand.size();
    }

    public void addCard(String card) {
        hand.add(card);
    }

    public int getScore() {
        return Deck.calculateHandValue(hand);
    }

    public List<String> getHand() {
        return new ArrayList<>(hand);
    }

    public void playerTurn(Scanner scanner) {
        message.cardsMessage(hand, getScore(), "Player");

        while (true) {
            if (getScore() > 21) {
                message.bustedMessage(getScore(), "Player");
                break;
            }

            if (getScore() == 21) {
                message.blackjack();
                break;
            }

            message.playerOption();
            if (!scanner.hasNextLine()) {
                message.noInputDetect();
                break;
            }
            String action = scanner.nextLine().toLowerCase();

            if (action.equals("hit")) {
                String newCard = deck.drawCard();
                addCard(newCard);
                message.newCardsMessage(newCard, "Player");
                message.cardsMessage(hand, getScore(), "Player");
            } else if (action.equals("stand")) {
                message.standsMessage(getScore(), "Player");
                break;
            } else {
                message.errorInputDetect();
            }
        }
    }
}

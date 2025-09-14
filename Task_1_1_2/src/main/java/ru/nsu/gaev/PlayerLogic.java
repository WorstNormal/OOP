package ru.nsu.gaev;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerLogic {
    private final List<String> hand = new ArrayList<>();
    private final Deck deck;
    private final Scanner scanner = new Scanner(System.in);

    public PlayerLogic(Deck deck) {
        this.deck = deck;
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

    // player turn
    public void playerTurn() {
        System.out.println("Your cards: " + hand + " (total = " + getScore() + ")");

        while (true) {
            if (getScore() > 21) {
                System.out.println("Bust! You have " + getScore() + " points.");
                break;
            }
            if (getScore() == 21) {
                System.out.println("You have 21! You stand automatically.");
                break;
            }
            System.out.print("Do you want to hit or stand? ");
            String action = scanner.next().toLowerCase();

            if (action.equals("hit")) {
                String newCard = deck.drawCard();
                addCard(newCard);
                System.out.println("You drew: " + newCard);
                System.out.println("Your cards: " + hand + " (total = " + getScore() + ")");
            } else if (action.equals("stand")) {
                System.out.println("You stand. Final score: " + getScore());
                break;
            } else {
                System.out.println("Please enter 'hit' or 'stand'.");
            }
        }
    }
}

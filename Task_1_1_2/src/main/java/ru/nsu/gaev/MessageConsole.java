package ru.nsu.gaev;

import java.util.List;

public class MessageConsole {
    // Dealer messages
    public void cardsMessage(List<String> hand, int score, String player) {
        System.out.println(player + " cards: " + hand + " (sum = " + score + ")");
    }

    public void newCardsMessage(String newCard, String player) {
        System.out.println(player + " drew a card: " + newCard);
    }

    public void bustedMessage(int score, String player) {
        System.out.println(player + " busted! (" + score + ")");
    }

    public void standsMessage(int score, String player) {
        System.out.println(player + " stands. Final score: " + score);
    }

    public void blackjack() {
        System.out.println("You have 21! You stand automatically.");
    }

    public void playerOption() {
        System.out.println("Do you want to hit or stand? ");
    }

    public void noInputDetect() {
        System.out.println("No input detected, standing automatically.");
    }

    public void errorInputDetect() {
        System.out.println("Please enter 'hit' or 'stand'.");
    }

    public void welcomeBlackjack(){
        System.out.println("Welcome to Blackjack!");
    }

    public void amountRound(Integer roundCounter){
        System.out.println("\n=== Round " + roundCounter + " ===");
    }

    public void playAgain(){
        System.out.print("Play again? (yes/no): ");
    }

    public void yesOrNo(){
        System.out.print("Please enter 'yes' or 'no': ");
    }

    public void gameOver(Integer roundCounter){
        System.out.println("Game over. You played " + roundCounter + " rounds.");
    }

    public void numberOfDecks(){
        System.out.print("Enter number of decks to use (at least 1): ");
    }

    public void playerBlackjack(){
        System.out.println("Blackjack! Player wins immediately!");
    }

    public void win(String player){
        System.out.println(player + "wins!");
    }

    public void tie(){
        System.out.println("Push (tie)!");
    }
}

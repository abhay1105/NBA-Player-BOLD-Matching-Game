package sample;

import java.util.ArrayList;

public class Deck {

    private ArrayList<Card> deck;
    private ArrayList<Card> discardPile;

    public Deck() {
        deck = new ArrayList<Card>();
        discardPile = new ArrayList<>();
    }

    // getter methods
    public ArrayList<Card> getDeck() { return deck; }

    public ArrayList<Card> getDiscardPile() { return discardPile; }

    // random number function
    public int randomNumber(int lowerBound, int upperBound) {
        return (int)(Math.floor((Math.random() * (upperBound - lowerBound + 1))) + lowerBound);
    }

    // deals a random card from the deck
    public Card dealCard() {
        int randNum = randomNumber(0, deck.size() - 1);
        Card randCard = deck.get(randNum);
        deck.remove(randNum);
        return randCard;
    }

    // moves a card into the discard pile of a deck
    public void discardCard(Card card) {
        discardPile.add(card);
    }

    // checks to see if there is another card to refill a spot on the table
    public boolean emptyDeck() {
        return deck.size() == 0;
    }

    // reshuffles the discard pile to create a new shuffled deck for a new game
    public void shuffleNewDeck() {
        getDeck().addAll(getDiscardPile());
    }

}

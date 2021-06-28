package sample;

import java.util.ArrayList;
import java.util.Arrays;

public class Player {

    private String name;
    private int score;
    private ArrayList<Card> cardsSelected;
    private ArrayList<String> sharedAttributes;

    public Player(String NAME) {
        name = NAME;
        score = 0;
        cardsSelected = new ArrayList<>();
        sharedAttributes = new ArrayList<>();
    }

    // getter methods
    public String getName() { return name; }

    public int getScore() { return score; }

    public ArrayList<Card> getCardsSelected() { return cardsSelected; }

    public ArrayList<String> getSharedAttributes() { return sharedAttributes; }

    // setter methods
    public void resetScore() { score = 0; }

    public void addSelectedCard(Card selectedCard) { cardsSelected.add(selectedCard); }

    public boolean compareCards() {
        if (getCardsSelected().size() > 1) {
            // card object reference for our first selected card
            Card firstCard = getCardsSelected().get(0);
            // array list to hold any shared attributes between cards; also adds all the first card attributes to the array
            // list because we are assuming that there is a match and we are trying to prove that there is no match
            sharedAttributes = new ArrayList<>(Arrays.asList(firstCard.getAttributes()));
            // loop will iterate through each selected card
            for (int i = 1;i < getCardsSelected().size();i++) {
                // card object reference will be used to compare every card to the first card
                Card nextCard = getCardsSelected().get(i);
                // loop will iterate through every attribute in the card via an array
                for (int j = 0;j < nextCard.getAttributes().length;j++) {
                    // more variables to store each attribute for easy comparison
                    String firstCardAtt = firstCard.getAttributes()[j];
                    String nextCardAtt = nextCard.getAttributes()[j];
                    // if the attributes don't match, then we remove them from the sharedAttributes ArrayList if it
                    // has already not been removed
                    if (!firstCardAtt.equals(nextCardAtt)) {
                        sharedAttributes.remove(firstCardAtt);
                    }
                }
                // if the sharedAttributes ArrayList becomes empty then we know that a match does not exist amongst
                // the user's selected cards
                if (sharedAttributes.size() == 0) {
                    return false;
                }
            }
            // prints out all shared attributes
            System.out.println("Shared:  ");
            for (String att: sharedAttributes) {
                System.out.println(att + "    ");
            }
        } else if (getCardsSelected().size() == 1) {
            Card firstCard = getCardsSelected().get(0);
            sharedAttributes = new ArrayList<>(Arrays.asList(firstCard.getAttributes()));
        }
        // if there is a match this will return true even when there is only 1 card, since 1 card is equal to 1 card
        return true;
    }

    // adds points depending on how many cards are selected and matched
    public void addPoints() {
        score += Math.pow(cardsSelected.size(), 2);
    }

    // removes all cards from the selected cards ArrayList, used when there is no match
    public void resetMatch() {
        cardsSelected.removeAll(cardsSelected);
    }
}

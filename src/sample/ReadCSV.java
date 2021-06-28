package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCSV {

    private String filePath;

    public ReadCSV() {
        filePath = "";
    }

    // function that reads and converts contents of a CSV file to Card objects
    public ArrayList<Card> readCard(ArrayList<Card> cardArray, String filePath) {
        String line = "";
        // the String in splitBy is what separates the tokens in a line in a csv file
        String splitBy = ",";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            // while a line is read
            while ((line = reader.readLine()) != null) {
                // string array of all the tokens in a line of the csv file
                String[] tokens = line.split(splitBy);
                // where the tokens in the String array get put into a card object
                Card tempCard = new Card(tokens[0], tokens[1], tokens[2], tokens[3]);
                // where we add each new card object to an array list of cards
                cardArray.add(tempCard);
            }
        } catch (IOException e) {
            // bypasses any errors caught while reading the csv file
            e.printStackTrace();
        }
        // returns the completed array list of pokemon
        return cardArray;
    }

}

package sample;

import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Card {

    private String name;
    private String position;
    private String team;
    private String playerShape;
    private Image playerIMG;
    private boolean flipped;
    private String[] attributes;

    public Card(String NAME, String POS, String TEAM, String SHAPE) throws FileNotFoundException {
        name = NAME;
        position = POS;
        team = TEAM;
        playerShape = SHAPE;
        playerIMG = new Image(new FileInputStream("src\\resources\\boldImages\\" + name + ".jpg"), 120, 80, false, false);
        flipped = false;
        attributes = new String[] {team, position, playerShape};
    }

    // getter methods
    public Image getPlayerIMG() { return playerIMG; }

    public String getName() { return name; }

    public String getPosition() { return position; }

    public String getTeam() { return team; }

    public String getPlayerShape() { return playerShape; }

    public boolean getFlipped() { return flipped; }

    public String[] getAttributes() { return attributes; }

    // testing purposes
    public String getInfo() { return name + "    " + position + "    " + team + "    " + playerShape; }

    // setter methods
    public void flipCard() { flipped = !flipped; }

    public void setFlipped(boolean flip) { flipped = flip; }

}

package sample;

// all necessary imports listed here
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

// Project Notes:
// 5 by 4 grid of cards for play
// 4 categories with 3 types in each category --> 81 cards total
// 2-4 players can play the game at a time
// Maybe computer player mode can be added later
// Points = (Number of cards matched) ^ 2
// button to submit match to see if they matched
// 2 dimensional array of image views/grid spots
// every time you click on a spot, you need to gather information about the location in the grid
// then you need to gather the characteristics of the card
// cards will go back to the deck if nothing matches between both cards
// object reference will be perfect
// assume match, prove its not a match
// compare by one characteristic at a time (first, second, third, etc.)
// image view array, array list of cards
// how do you connect a click to a specific card
// comparison
// SHOE BRANDS? SHAPE?

// A+ worthy?
// show what matches
// add some pauses
// computer player

// click button turn moves up one number, but gets reset when compared to a mod of the numbers of players

public class Controller {

    // all dynamic fx elements initialized here
    @FXML
    Label lblPlayerTurn, lblPlayerScores, lblDeckCount, lblCardInfo, lblCategories, lblTeamCat, lblPositionCat,
            lblShapeCat, lblMode, lblCurrentPlayers, lblWinners;
    @FXML
    Button btnStart, btnFinishTurn, btnComputerMode, btnMultiplayerMode, btnAddPlayer, btnContinue;
    @FXML
    GridPane grdCardTable, grdTeamCategory, grdPositionCategory, grdShapeCategory;
    @FXML
    Rectangle recCardInfo, recPlayerTurn, recPlayerScore, recDeckCount, recCategories, recTeamCat, recPositionCat,
            recShapeCat;
    @FXML
    TextField txtNameField;
    @FXML
    ImageView imgGameOver;

    // global variables
    private Deck deck = new Deck();
    private Image cardBack;
    private Image gameOver;

    private ArrayList<String> teams = new ArrayList<>();
    private ArrayList<String> positions = new ArrayList<>();
    private ArrayList<String> shapes = new ArrayList<>();

    private ImageView [][] cardGridIMG = new ImageView[5][4];
    private Card [][] cardArray = new Card[5][4];

    private String [][] teamGrid = new String[3][5];
    private String [][] positionGrid = new String[3][2];
    private String [][] shapeGrid = new String[3][2];

    private ImageView [][] teamGridIMG = new ImageView[3][5];
    private ImageView [][] positionGridIMG = new ImageView[3][2];
    private ImageView [][] shapeGridIMG = new ImageView[3][2];

    private int numPlayers;
    private ArrayList<Player> players = new ArrayList<>();
    private int turn = 0;

    // function that runs on the opening of the FXML file
    @FXML
    public void initialize() throws FileNotFoundException {
        // creating our cards
        cardBack = new Image(new FileInputStream("src\\resources\\boldIcons\\cardBack.jpg"));
        ReadCSV scanner = new ReadCSV();
        scanner.readCard(deck.getDeck(), "src\\resources\\boldPlayersCSV2.csv");

        // for testing purposes
//        for (int i = 0;i < 45;i++) {
//            deck.discardCard(deck.dealCard());
//        }

        gameOver = new Image(new FileInputStream("src\\resources\\boldIcons\\gameOver.png"));

        showPlayerSetup(true);
        showGame(false);
        showEndGame(false);

        // filling in the teams, positions, and shapes arrays
        for (Card card: deck.getDeck()) {
            if (!teams.contains(card.getTeam())) {
                teams.add(card.getTeam());
            }
            if (!positions.contains(card.getPosition())) {
                positions.add(card.getPosition());
            }
            if (!shapes.contains(card.getPlayerShape())) {
                shapes.add(card.getPlayerShape());
            }
        }

        // filling in the team category GridPane
        int counter = 0;
        for (int i = 0;i < teamGridIMG.length;i++) {
            for (int j = 0;j < teamGridIMG[i].length;j++) {
                teamGridIMG[i][j] = new ImageView();
                teamGridIMG[i][j].setFitHeight(50);
                teamGridIMG[i][j].setFitWidth(65);
                grdTeamCategory.add(teamGridIMG[i][j], i, j);
                if (counter <= teams.size() - 1) {
                    teamGrid[i][j] = teams.get(counter);
                    teamGridIMG[i][j].setImage(new Image(new FileInputStream("src\\resources\\boldTeams\\" + teams.get(counter) + ".png")));
                }
                counter++;
            }
        }

        counter = 0;
        // filling in the position and shape category GridPanes
        for (int i = 0;i < shapeGridIMG.length;i++) {
            for (int j = 0;j < shapeGridIMG[i].length;j++) {
                shapeGridIMG[i][j] = new ImageView();
                shapeGridIMG[i][j].setFitHeight(40);
                shapeGridIMG[i][j].setFitWidth(60);
                grdShapeCategory.add(shapeGridIMG[i][j], i, j);
                if (counter <= shapes.size() - 1) {
                    shapeGrid[i][j] = shapes.get(counter);
                    shapeGridIMG[i][j].setImage(new Image(new FileInputStream("src\\resources\\boldIcons\\" + shapes.get(counter) + ".png")));
                }

                positionGridIMG[i][j] = new ImageView();
                positionGridIMG[i][j].setFitHeight(40);
                positionGridIMG[i][j].setFitWidth(60);
                grdPositionCategory.add(positionGridIMG[i][j], i, j);
                if (counter <= positions.size() - 1) {
                    positionGrid[i][j] = positions.get(counter);
                    positionGridIMG[i][j].setImage(new Image(new FileInputStream("src\\resources\\boldPositions\\" + positions.get(counter) + ".png")));
                }
                counter++;
            }
        }
        txtNameField.setDisable(true);
        btnAddPlayer.setDisable(true);
    }

    // toggles all fx elements related to the actual game
    public void showGame(boolean show) {
        Rectangle[] backgroundRec = new Rectangle[] {recCardInfo, recPlayerTurn, recPlayerScore, recDeckCount,
                recCategories, recTeamCat, recPositionCat, recShapeCat};
        Label[] labels = new Label[] {lblCardInfo, lblPlayerTurn, lblPlayerScores, lblDeckCount, lblCategories,
                lblTeamCat, lblPositionCat, lblShapeCat};
        GridPane[] gridPanes = new GridPane[] {grdCardTable, grdTeamCategory, grdPositionCategory, grdShapeCategory};
        for (Rectangle rec: backgroundRec) {
            rec.setVisible(show);
        }
        for (Label lbl: labels) {
            lbl.setVisible(show);
        }
        for (GridPane grd: gridPanes) {
            grd.setVisible(show);
        }
        btnFinishTurn.setVisible(show);
        btnStart.setDisable(show);
    }

    // toggles all fx elements related to player setup
    public void showPlayerSetup(boolean show) {
        lblMode.setVisible(show);
        btnComputerMode.setVisible(show);
        btnMultiplayerMode.setVisible(show);
        txtNameField.setVisible(show);
        btnAddPlayer.setVisible(show);
    }

    // toggles all fx elements related to the ending of a game
    public void showEndGame(boolean show) {
        imgGameOver.setVisible(show);
        lblWinners.setVisible(show);
        btnContinue.setVisible(show);
    }

    // random number function
    public int randomNumber(int lowerBound, int upperBound) {
        return (int)(Math.floor((Math.random() * (upperBound - lowerBound + 1))) + lowerBound);
    }

    // global variable to store mode of game
    String mode = "";

    // runs when the player clicks the computer mode
    public void computerMode() {
        mode = "COMPUTER";
        btnComputerMode.setDisable(true);
        btnMultiplayerMode.setDisable(true);
        txtNameField.setDisable(false);
        btnAddPlayer.setDisable(false);
        btnAddPlayer.setText("ENTER NAME");
        players.add(new Player("Computer"));
        numPlayers = players.size();
        lblMode.setText("Enter your name. Click START GAME once finished.");
    }

    // runs when the player clicks the multiplayer mode
    public void multiplayerMode() {
        mode = "MULTIPLAYER";
        btnComputerMode.setDisable(true);
        btnMultiplayerMode.setDisable(true);
        txtNameField.setDisable(false);
        btnAddPlayer.setDisable(false);
        lblMode.setText("Add players to game. Click START GAME once finished.");
    }

    // adds a player to the players ArrayList for multiplayer mode
    public void addPlayer() {
        String name = txtNameField.getText();
        txtNameField.setText("");
        players.add(new Player(name));
        numPlayers = players.size();
        if (mode.equals("COMPUTER")) {
            txtNameField.setDisable(true);
            btnAddPlayer.setDisable(true);
        } else {
            String allNames = "Current Players: ";
            for (Player player: players) {
                allNames += "\n" + "---> " + player.getName();
            }
            lblCurrentPlayers.setText(allNames);
            lblCurrentPlayers.setVisible(true);
        }
    }

    // starts game once all modes and players involved have been decided
    public void startGame() {
        numPlayers = players.size();
        btnStart.setDisable(true);
        lblCurrentPlayers.setVisible(false);
        showPlayerSetup(false);
        setupTable();
        flipAllFaceDown();
        updateAll();
    }

    // sets up the game by placing ImageViews into a GridPane for the cards
    public void setupTable() {
        showGame(true);
        for (int i = 0;i < cardGridIMG.length;i++) {
            for (int j = 0;j < cardGridIMG[i].length;j++) {
                cardGridIMG[i][j] = new ImageView();
                cardGridIMG[i][j].setFitHeight(144);
                cardGridIMG[i][j].setFitWidth(96);
                cardGridIMG[i][j].setOnMouseClicked(z);
                grdCardTable.add(cardGridIMG[i][j], i, j);
            }
        }
        if (mode.equals("COMPUTER")) {
            turn = 1;
        }
        dealCards();
        updateTable();
        updateAll();
    }

    // deals cards into the two-dimensional array of ImageViews ("our table")
    public void dealCards() {
        for (int i = 0;i < cardArray.length;i++) {
            for (int j = 0;j < cardArray[i].length;j++) {
                cardArray[i][j] = deck.dealCard();
            }
        }
    }

    // displays the current state of the board
    public void updateTable() {
        for (int i = 0;i < cardArray.length;i++) {
            for (int j = 0;j < cardArray[i].length;j++) {
                Card tempCard = cardArray[i][j];
                if (tempCard.getFlipped()) {
                    cardGridIMG[i][j].setImage(tempCard.getPlayerIMG());
                } else {
                    cardGridIMG[i][j].setImage(cardBack);
                }
            }
        }
    }

    // flips all cards face down when there is no match
    public void flipAllFaceDown() {
        for (int i = 0;i < cardArray.length;i++) {
            for (int j = 0;j < cardArray[i].length;j++) {
                Card tempCard = cardArray[i][j];
                tempCard.setFlipped(false);
                cardGridIMG[i][j].setImage(cardBack);
            }
        }
    }

    // variables to store location of clicked card
    private int row, col;

    // handles the click of a card in the GridPane
    EventHandler z = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent t) {

            // this if-statement is needed because the computer is technically the first Player in the players ArrayList
            if (mode.equals("COMPUTER")) { turn = 1; }

            col = grdCardTable.getRowIndex((ImageView) t.getSource());
            row = grdCardTable.getColumnIndex((ImageView) t.getSource());
            Card clickedCard = cardArray[row][col];

            Player currentPlayer = players.get(turn);
            if (!currentPlayer.getCardsSelected().contains(clickedCard)) {
                currentPlayer.addSelectedCard(clickedCard);
            }
            if (currentPlayer.compareCards()) {
                ArrayList<String> sharedAttributes = new ArrayList<>(currentPlayer.getSharedAttributes());
                for (String sharedAttribute: sharedAttributes) {
                    System.out.println("shared: " + sharedAttribute);
                }
                showMatch(sharedAttributes);
                clickedCard.flipCard();
            } else {

                showMatch(new ArrayList<>());

                clickedCard.flipCard();
                System.out.println(clickedCard.getFlipped());
                updateTable();

                // here we will store what the user had before their match was messed up so we can use it as
                // intelligence for a computer opponent
                ArrayList<Card> userCards = new ArrayList<>(currentPlayer.getCardsSelected());

                for (Card card: userCards) {
                    System.out.println("selected: " + card.getInfo());
                }

                // basically using this to tell the computer when to run a certain piece of code by giving the
                // timer.schedule() a task as well as a delay. In actuality, the code does not stop, instead it comes
                // back when the time has come.
                Timer timer = new Timer();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        flipAllFaceDown();
                        currentPlayer.resetMatch();
                    }
                };
                // compiler will wait 1 second before executing the code in the task
                int delay = 1000;
                timer.schedule(task, delay);

                turn = (turn + 1) % numPlayers;
                updateAll();

                // identical to a timer, however using a Timeline allows us to update JavaFX elements which a normal
                // Timer cannot do for us
                Timeline timeline = new Timeline(new KeyFrame(
                        Duration.millis(2500),
                        ae -> {
                            if (mode.equals("COMPUTER")) { computerTurn(userCards); }
                        } ));
                timeline.play();
            }

            updateTable();
            lblCardInfo.setText(clickedCard.getInfo());
        }
    };

    // conducts the computer turn by utilizing an ArrayList of Cards gathered from the user's previous turn
    public void computerTurn(ArrayList<Card> userCards) {
        // computer will always be turn 0 in the single player mode
        turn = 0;
        Player currentPlayer = players.get(turn);

        if (userCards.size() >= 3) {
            // intelligent turn (this will occur when the computer sees a possible match during the previous user's turn,
            // in which it will utilize that and continue to pick more based on chance)
            userCards.remove(userCards.size() - 1);
            for (Card card: userCards) {
                currentPlayer.addSelectedCard(card);
            }
            System.out.println("Intelligent move run");
        } else {
            // random turn (this will occur when the computer doesn't really know what cards to even attempt to pick at
            // all, for example, right after a user gets a match)

            // will pick two cards at random first
            for (int i = 0;i < 2;i++) {
                int row = randomNumber(0, cardArray.length - 1);
                int col = randomNumber(0, cardArray[0].length - 1);
                while (currentPlayer.getCardsSelected().contains(cardArray[row][col])) {
                    row = randomNumber(0, cardArray.length - 1);
                    col = randomNumber(0, cardArray[0].length - 1);
                }
                currentPlayer.addSelectedCard(cardArray[row][col]);
            }

            int chance = randomNumber(1, 10);
            // if the two cards are a match
            if (currentPlayer.compareCards()) {
                if (chance >= 1 && chance <= 4) {
                    // 40% chance that the user will continue picking cards
                    while (currentPlayer.getCardsSelected().contains(cardArray[row][col])) {
                        row = randomNumber(0, cardArray.length - 1);
                        col = randomNumber(0, cardArray[0].length - 1);
                    }
                    currentPlayer.addSelectedCard(cardArray[row][col]);

                    chance = randomNumber(1, 10);
                    // if the three cards are a match
                    if (currentPlayer.compareCards()) {
                        if (chance >= 1 && chance <= 3) {
                            // 30% chance that the user will continue picking cards
                            while (currentPlayer.getCardsSelected().contains(cardArray[row][col])) {
                                row = randomNumber(0, cardArray.length - 1);
                                col = randomNumber(0, cardArray[0].length - 1);
                            }
                            currentPlayer.addSelectedCard(cardArray[row][col]);

                            chance = randomNumber(1, 10);
                            // if the four cards are a match
                            if (currentPlayer.compareCards()) {
                                if (chance >= 1 && chance <= 2) {
                                    // 20% chance that the user will continue picking cards
                                    while (currentPlayer.getCardsSelected().contains(cardArray[row][col])) {
                                        row = randomNumber(0, cardArray.length - 1);
                                        col = randomNumber(0, cardArray[0].length - 1);
                                    }
                                    currentPlayer.addSelectedCard(cardArray[row][col]);

                                    // getting a five card match is very rare, so I ended the process here
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Card card: currentPlayer.getCardsSelected()) {
            card.flipCard();
            System.out.println(card.getInfo());
        }
        updateTable();

        showMatch(currentPlayer.getSharedAttributes());

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> {
                    if (currentPlayer.compareCards()) {
                        finishTurn();
                    }
                    turn = 1;
                    updateAll();
                    currentPlayer.resetMatch();
                    flipAllFaceDown();
                    System.out.println("first timer");
                } ));
        timeline.play();

        // quick note: for the single player game mode the turn "0" is always the computer and the turn "1" is always the user
    }

    // ends turn of current player, moves onto the next player
    public void finishTurn() {
        boolean gameOver = false;
        Player currentPlayer = players.get(turn);
        if (currentPlayer.getCardsSelected().size() > 1) {
            currentPlayer.addPoints();
            for (Card card: currentPlayer.getCardsSelected()) {
                deck.discardCard(card);
                if (deck.emptyDeck()) {
                    gameOver = true;
                } else {
                    replaceCard(card);
                    if (mode.equals("MULTIPLAYER")) {
                        flipAllFaceDown();
                        updateTable();
                    }
                }
            }
        }
        if (gameOver) {
            endGame();
        } else {
            players.get(turn).resetMatch();
            turn = (turn + 1) % numPlayers;
            updateAll();
            if (mode.equals("COMPUTER") && turn == 0) {
                computerTurn(new ArrayList<>());
            }
        }
    }

    // finds a card in the cardsArray and replaces it with a new card
    public void replaceCard(Card card) {
        for (int i = 0;i < cardArray.length;i++) {
            for (int j = 0;j < cardArray[i].length;j++) {
                Card tempCard = cardArray[i][j];
                if (tempCard.equals(card)) {
                    cardArray[i][j] = deck.dealCard();
                }
            }
        }
    }

    // ends game and displays scores and winners
    public void endGame() {
        // display the winner based on highest score
        int bestScore = 0;
        for (Player player: players) {
            if (player.getScore() > bestScore) {
                bestScore = player.getScore();
            }
        }
        String winners = "";
        for (Player player: players) {
            if (player.getScore() == bestScore) {
                winners += player.getName();
            }
        }
        winners += " WON THE GAME";
        imgGameOver.setImage(gameOver);
        lblWinners.setText(winners);
        showEndGame(true);
        showGame(false);
        showPlayerSetup(false);

        // resetting everything for a possible new game

        // resetting players in game
        players = new ArrayList<>();
        btnStart.setText("PLAY AGAIN");

        // taking all remaining cards on the table and shuffling everything for a new deck
        for (Card[] cards : cardArray) {
            for (Card card : cards) {
                if (!deck.getDiscardPile().contains(card)) {
                    deck.discardCard(card);
                }
            }
        }
        deck.shuffleNewDeck();

        // resetting turn back to first person for next game
        turn = 0;
    }

    // transitions from win screen to new game screen
    public void transitionToNewGame() {
        btnStart.setDisable(false);
        showPlayerSetup(true);
        showEndGame(false);
        btnMultiplayerMode.setDisable(false);
        btnComputerMode.setDisable(false);
        btnAddPlayer.setDisable(true);
        txtNameField.setDisable(true);
    }

    // updates the score for all players
    public void updateScores() {
        String allScores = "";
        for (Player user: players) {
            allScores += user.getName() + "'s Score: " + user.getScore() + "\n";
        }
        lblPlayerScores.setText(allScores);
    }

    // updates all statistics: remaining deck size, score, and turn
    public void updateAll() {
        updateScores();
        lblPlayerTurn.setText(players.get(turn).getName() + "'s Turn");
        if (deck.getDeck().size() == 0) {
            lblDeckCount.setText("LAST TURN!!!");
        } else {
            lblDeckCount.setText("Deck Count: " + deck.getDeck().size());
        }
    }

    // shows the specific attribute that is matching by highlighting them in the GridPanes
    public void showMatch(ArrayList<String> sharedAttributes) {
        System.out.println("Show Match");
        for (String shared: sharedAttributes) {
            System.out.println(shared);
        }
        // will show any matches related to the team category
        for (int i = 0;i < teamGridIMG.length;i++) {
            for (int j = 0;j < teamGridIMG[i].length;j++) {
                ImageView teamImage = teamGridIMG[i][j];
                String teamName = teamGrid[i][j];
                System.out.println("teamName: " + teamName + "      sharedAttributes: " + sharedAttributes.size());
                if (sharedAttributes.contains(teamName)) {
                    System.out.println("color changed for team");
                    teamImage.getStyleClass().clear();
                    teamImage.setStyle("-fx-background-color: darkred; -fx-border-color: black; -fx-border-width: 4;");
                    teamImage.setVisible(true);
                } else {
                    teamImage.getStyleClass().clear();
                    teamImage.setStyle("-fx-border-width: 0;");
                    teamImage.setVisible(false);
                }
            }
        }

        // will show any matches related to the shape or position categories
        for (int i = 0;i < shapeGridIMG.length;i++) {
            for (int j = 0;j < shapeGridIMG[i].length;j++) {
                String positionName = positionGrid[i][j];
                String shapeName = shapeGrid[i][j];
                ImageView positionImage = positionGridIMG[i][j];
                ImageView shapeImage = shapeGridIMG[i][j];
                if (sharedAttributes.contains(positionName)) {
                    System.out.println("color changed for position");
                    positionImage.getStyleClass().clear();
                    positionImage.setStyle("-fx-background-color: darkred; -fx-border-color: black; -fx-border-width: 4;");
                    positionImage.setVisible(true);
                } else {
                    positionImage.getStyleClass().clear();
                    positionImage.setStyle("-fx-border-width: 0;");
                    positionImage.setVisible(false);
                }
                if (sharedAttributes.contains(shapeName)) {
                    System.out.println("color changed for shape");
                    shapeImage.getStyleClass().clear();
                    shapeImage.setStyle("-fx-background-color: darkred; -fx-border-color: black; -fx-border-width: 4;");
                    shapeImage.setVisible(true);
                } else {
                    shapeImage.getStyleClass().clear();
                    shapeImage.setStyle("-fx-border-width: 0;");
                    shapeImage.setVisible(false);
                }
            }
        }
    }
}
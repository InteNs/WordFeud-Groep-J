package models;

import enumerations.BoardType;
import enumerations.GameState;
import enumerations.Language;
import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private int id;
    private ArrayList<Message> messages;
    private GameState gameState;
    private User opponent;
    private User challenger;
    private Language language;
    private BoardType boardType;
    private ArrayList<Turn> turns; //TODO apply these turns to fields when building board
    private Field[][] defaultGameBoard; //SHOULD NOT BE OVERWRITTEN
    private Field[][] changeableGameBoard; //USE THIS INSTEAD
    private ArrayList<Field>fieldsChangedThisTurn;

    public Game(int id, User challenger, User opponent, GameState state, BoardType boardType, Language language) {
        this.id = id;
        this.challenger = challenger;
        this.opponent = opponent;
        this.gameState = state;
        this.language = language;
        this.boardType = boardType;
    }

    public Game(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int setMessages(ArrayList<Message> messages) {
        int diff = 0;
        if(this.messages != null) diff = messages.size() - this.messages.size();
        this.messages = messages;
        return diff;
    }

    public int setTurns(ArrayList<Turn> turns) {
        int diff = 0;
        if(this.turns != null) diff = turns.size() - this.turns.size();
        this.turns = turns;
        return diff;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public ArrayList<User> getPlayers() {
        return new ArrayList<>(Arrays.asList(challenger, opponent));
    }

    public boolean hasPlayer(User user) {
        return getPlayers().contains(user);
    }

    public GameState getGameState() {
        return gameState;
    }

    public BoardType getBoardType() {
        return boardType;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    //Sets default board
    public void setBoard(Field[][] fields){
        this.defaultGameBoard = fields;
        changeableGameBoard=defaultGameBoard;
    }

    //Used to display a certain turn on the GameBoard
    public void setBoardStateTo(Turn turnToDisplay){
        changeableGameBoard=defaultGameBoard;
        for (Turn turn : turns)
            if (turn.equals(turnToDisplay))
                break;
            else {
                for (Tile tile : turn.getPlacedTiles()
                        ) {
                    changeableGameBoard[tile.getX()-1][tile.getY()-1].setTile(tile);
                }
            }
    }

    public ArrayList<Tile> getPlacedTiles(){
        ArrayList<Tile> tiles = new ArrayList<>();
        turns.stream().filter(turn -> turn.getPlacedTiles() != null).forEach(turn -> {
            tiles.addAll(turn.getPlacedTiles());
        });
        return tiles;
    }

    //Check if a field already has a tile
    public boolean placeTile(Tile tile,Field field){
        if (field.getTile()==null){
            field.setTile(tile);
            return true;
        }
        return false;
    }

    private void getTilesPlacedThisTurn(){
        fieldsChangedThisTurn = new ArrayList<>();
        for (int i = 0; i < changeableGameBoard.length; i++) {
            for (int j = 0; j < changeableGameBoard.length; j++) {
                if (!getPlacedTiles().contains(changeableGameBoard[i][j].getTile()) && changeableGameBoard[i][j].getTile()!= null){
                    fieldsChangedThisTurn.add(changeableGameBoard[i][j]);
                }
            }
        }
    }

    public boolean verifyCurrentTurn(){
        //check all the fields changed/Tiles placed this turn, see if their x or y values are the same, also check if tiles are connected
        //yes --> currentTurn is still valid

        boolean validTurn=true;
        String direction; //0--HOR 1--VER
        int x = fieldsChangedThisTurn.get(0).getTile().getX();
        int y= fieldsChangedThisTurn.get(0).getTile().getY();
        if (Collections.frequency(fieldsChangedThisTurn,x)!=fieldsChangedThisTurn.size()){ //Checks if all X-values are NOT the same for each placed letter
            if ( Collections.frequency(fieldsChangedThisTurn,y)!=fieldsChangedThisTurn.size()) { //Checks if all Y-values are NOT the same for each placed letter
                validTurn = false;
            }
            direction="x";
        } else {
            direction ="y";
        }

        fieldsChangedThisTurn.sort(Comparator.comparing(field -> field.getTile().getX()));

        ArrayList<Integer> xValues=fieldsChangedThisTurn.stream()
                .map(field -> field.getTile().getX())
                .collect(Collectors.toCollection(ArrayList<Integer>::new));

        ArrayList<Integer> yValues = fieldsChangedThisTurn.stream()
                .map(field -> field.getTile().getY())
                .collect(Collectors.toCollection(ArrayList<Integer>::new));

        if (direction.equalsIgnoreCase("x")) {
            for (x = Collections.min(xValues); x <= Collections.max(xValues); x++) {
                if (changeableGameBoard[x][y].getTile() == null && !xValues.contains(x))
                    validTurn = false;

            }
        } else {
            for (y = Collections.min(yValues); y <= Collections.max(yValues); y++) {
                x = fieldsChangedThisTurn.get(0).getTile().getY();
                if (changeableGameBoard[x][y].getTile() == null && !xValues.contains(y))
                    validTurn = false;

            }
        }
        return validTurn;
    }


    //TODO:
    public int calculateWordScore(Field field){
        int wordScore=0;
        //Ask which words can be made with the addition of the recently placed tile
        
        //HORIZONTAL CHECK --RIGHT
        for (int x=field.getTile().getX()-1; x <changeableGameBoard.length ; x++) {
            for (int y =field.getTile().getY()-1; y <changeableGameBoard.length ; y++) {

            }
        }

        //HORIZONTAL CHECK --LEFT
        for (int x=field.getTile().getX()-1; x >=0; x--) {
            for (int y =field.getTile().getY()-1; y >0 ; y--) {

            }
        }

        //VERTICAL CHECK --UP

        //VERTICAL CHECK --DOWN
        
        //for each word that can be made, calculate the score of that word
        //Add the score to the wordScore


        return wordScore;
    }



    @Override
    public String toString() {
        return "[" + id + "]["+language+"] " +boardType.toString().toLowerCase()
                +" spel tussen " + challenger + " en " + opponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return id == game.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}

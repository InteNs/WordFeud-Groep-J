package models;


import enumerations.FieldType;
import enumerations.TurnType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

public class TurnBuilder {

    private Field[][] gameBoard;
    private ObservableList<Tile> currentRack;
    private ObservableList<Field> fieldsChangedThisTurn;
    private ArrayList<ArrayList<Field>> listOfFieldsWithWordsThisTurn;
    private int scoreThisTurn;


    public TurnBuilder(Field[][] gameBoard, ObservableList<Tile> currentRack) {
        this.gameBoard = gameBoard;
        this.currentRack = currentRack;
        this.fieldsChangedThisTurn = FXCollections.observableArrayList();
        this.fieldsChangedThisTurn.addListener((ListChangeListener<? super Field>) observable -> {
            verifyAndCalculate();
        });
    }

    public Turn buildTurn(int newTurnId, User user, TurnType turnType){
       return new Turn(newTurnId,
                getScoreThisTurn(),
                user,
                turnType,
                getTilesChangedThisTurn(),
                new ArrayList<>(getCurrentRack()));
    }


    private void verifyAndCalculate() {
        listOfFieldsWithWordsThisTurn = null;
        if (!fieldsChangedThisTurn.isEmpty()) {
            Character fixedAxis = verifyCurrentTurn();
            if (fixedAxis != null) {
                listOfFieldsWithWordsThisTurn = resolveWords(fixedAxis);
                scoreThisTurn = calculateTotalScore(listOfFieldsWithWordsThisTurn);
            }
        }
    }

    public int getScoreThisTurn() {
        return scoreThisTurn;
    }

    public ArrayList<Tile> getTilesChangedThisTurn() {
        return fieldsChangedThisTurn.stream()
                .map(Field::getTile)
                .collect(Collectors.toCollection(ArrayList<Tile>::new));
    }

    public ArrayList<ArrayList<Field>> getListOfFieldsWithWordsThisTurn() {
        return listOfFieldsWithWordsThisTurn;
    }

    public ArrayList<String> getWordsFoundThisTurn() {
        ArrayList<String> words = new ArrayList<>();
        listOfFieldsWithWordsThisTurn.forEach(fields -> {
            StringBuilder newWord = new StringBuilder();
            fields.stream()
                    .map(Field::getTile).forEach(tile -> newWord.append(tile.toString()));
            words.add(newWord.toString());
        });
        return words;
    }


    public ObservableList<Tile> getCurrentRack() {
        return currentRack;
    }

    public ObservableList<Field> getFieldsChangedThisTurn() {
        return fieldsChangedThisTurn;
    }

    public Field[][] getGameBoard() {
        return gameBoard;
    }

    public void addPlacedTile(Field field, Tile tile) {
        field.setTile(tile);
        tile.setX(field.getX());
        tile.setY(field.getY());
        fieldsChangedThisTurn.add(field);
    }

    public void removePlacedTile(Field field) {
        field.getTile().clearCoordinates();
        field.setTile(null);
        fieldsChangedThisTurn.remove(field);
    }

    /**
     * check if the current state of the game is a valid turn
     *
     * @return validTurn
     */
    public Character verifyCurrentTurn() {
        boolean validTurn = true;
        char fixedAxis = 0;

        if (fieldsChangedThisTurn.size() == 0 || startFieldIsEmpty()) {
            validTurn = false;
        }

        if (validTurn) {

        /* fetch first X and all X coords */
            int x = fieldsChangedThisTurn.get(0).getX();
            ArrayList<Integer> xValues = fieldsChangedThisTurn.stream()
                    .map(Field::getX)
                    .collect(Collectors.toCollection(ArrayList<Integer>::new));

        /* fetch first Y and All X coords */
            int y = fieldsChangedThisTurn.get(0).getY();
            ArrayList<Integer> yValues = fieldsChangedThisTurn.stream()
                    .map(Field::getY)
                    .collect(Collectors.toCollection(ArrayList<Integer>::new));

        /* check if placed on single axis, if not -> invalidate turn */
            if (Collections.frequency(xValues, x) != xValues.size()) {
                if (Collections.frequency(yValues, y) != yValues.size())
                    validTurn = false;  // Y and X are not equal for all tiles

                fixedAxis = 'y';        // Y is equal for all tiles
            } else
                fixedAxis = 'x';        // X is equal for all tiles

        /* check if there are gaps between placed letters */
            if (fixedAxis == 'y') {
                // check if all X coords are not null
                for (x = Collections.min(xValues); x <= Collections.max(xValues); x++)
                    if (gameBoard[y][x].getTile() == null && !xValues.contains(x))
                        validTurn = false;
            } else {
                // check if all Y coords are not null
                for (y = Collections.min(yValues); y <= Collections.max(yValues); y++)
                    if (gameBoard[y][x].getTile() == null && !yValues.contains(y))
                        validTurn = false;
            }
        }

        if (validTurn) {
            if (fieldsChangedThisTurn.size() == 1 && findWords(checkColumn(fieldsChangedThisTurn.get(0).getX())) == null) {
                if (findWords(new ArrayList<>(Arrays.asList(gameBoard[fieldsChangedThisTurn.get(0).getY()]))) == null) {
                    validTurn = false;
                } else {
                    fixedAxis = 'y';
                }
            }
        }

        if (validTurn) {
            return fixedAxis;
        } else {
            return null;
        }

    }

    public ArrayList<ArrayList<Field>> resolveWords(char fixedAxis) {
        ArrayList<ArrayList<Field>> wordsFound = new ArrayList<>();
        ArrayList<Field> word;
        switch (fixedAxis) {
            case 'x':
                int xPos = getFieldsChangedThisTurn().get(0).getX();
                wordsFound.add(findWords(checkColumn(xPos)));
                for (Field field : getFieldsChangedThisTurn()) {
                    word = findWords(new ArrayList<>(Arrays.asList(gameBoard[field.getY()])));
                    if (word != null) {
                        wordsFound.add(word);
                    }
                }
                break;
            case 'y':
                int yPos = getFieldsChangedThisTurn().get(0).getY();
                wordsFound.add(findWords(new ArrayList<>(Arrays.asList(gameBoard[yPos]))));
                for (Field field : fieldsChangedThisTurn) {
                    word = findWords(checkColumn(field.getX()));
                    if (word != null) {
                        wordsFound.add(word);
                    }
                }
                break;
            default:
                break;
        }
        return wordsFound;
    }

    private int calculateTotalScore(ArrayList<ArrayList<Field>> wordsFound) {
        int totalScore = 0;
        for (ArrayList<Field> word : wordsFound) {
            totalScore += calculateWordScore(word);
        }
        if (fieldsChangedThisTurn.size() == 7) {
            totalScore += 40;
        }
        return totalScore;
    }

    private int calculateWordScore(ArrayList<Field> word) {
        int wordScore = 0;
        for (Field field : word) {
            if (fieldsChangedThisTurn.contains(field) && (field.getFieldType() == FieldType.DL || field.getFieldType() == FieldType.TL)) {
                if (field.getFieldType() == FieldType.TL) {
                    wordScore += field.getTile().getValue() * 3;
                } else {
                    wordScore += field.getTile().getValue() * 2;
                }
            } else {
                wordScore += field.getTile().getValue();
            }
        }
        for (Field field : word) {
            if (fieldsChangedThisTurn.contains(field) && (field.getFieldType() == FieldType.DW || field.getFieldType() == FieldType.TW)) {
                if (field.getFieldType() == FieldType.TW) {
                    wordScore = wordScore * 3;
                } else {
                    wordScore = wordScore * 2;
                }
            }
        }
        return wordScore;
    }

    private ArrayList<Field> findWords(ArrayList<Field> turnFields) {
        ArrayList<Field> word = new ArrayList<>();
        for (Field field : turnFields) {
            if (field.getTile() != null) {
                word.add(field);
            } else {
                for (Field wordField : word) {
                    if (fieldsChangedThisTurn.contains(wordField) && word.size() > 1) {
                        return word;
                    }
                }
                word.clear();
            }
        }
        return null;
    }

    private ArrayList<Field> checkColumn(int xPos) {
        ArrayList<Field> returnColumn = new ArrayList<>();
        for (int y = 0; y < gameBoard.length; y++) {
            returnColumn.add(gameBoard[y][xPos]);
        }
        return returnColumn;
    }


    private boolean startFieldIsEmpty() {
        for (int y = 0; y < gameBoard.length; y++)
            for (int x = 0; x < gameBoard.length; x++)
                if (gameBoard[y][x].getFieldType() == FieldType.STARTTILE)
                    return gameBoard[y][x].getTile() == null;
        return false;
    }

    public void fillCurrentRack(ObservableList<Tile> playingPot) {
        for (int i = 0; i < 8 - currentRack.size(); i++) {
            if (!playingPot.isEmpty())
                currentRack.add(playingPot.get(new Random().nextInt(playingPot.size())));
        }
    }
}

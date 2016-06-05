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
    private ObservableList<Field> fieldsChanged;
    private ArrayList<ArrayList<Field>> listOfFieldsWithWords;
    private int score;
    private ObservableList<Tile> pot;


    public TurnBuilder(Field[][] gameBoard, ObservableList<Tile> currentRack) {
        this.gameBoard = gameBoard;
        this.currentRack = currentRack;
        this.listOfFieldsWithWords = new ArrayList<>();
        this.fieldsChanged = FXCollections.observableArrayList();
        this.pot = FXCollections.observableArrayList();
        this.fieldsChanged.addListener((ListChangeListener<? super Field>) observable -> verifyAndCalculate());
    }

    public TurnBuilder() {
        this.listOfFieldsWithWords = new ArrayList<>();
        this.fieldsChanged = FXCollections.observableArrayList();
    }

    public ArrayList<Turn> buildBeginTurns(Game selectedGame) {
        ArrayList<Turn> returnList = new ArrayList<>();
        Turn firstBeginTurn = new Turn(1, 0, selectedGame.getChallenger(), TurnType.BEGIN);
        Turn secondBeginTurn = new Turn(2, 0, selectedGame.getOpponent(), TurnType.BEGIN);
        returnList.add(firstBeginTurn);
        returnList.add(secondBeginTurn);
        return returnList;
    }

    public ArrayList<Turn> buildEndTurns(Turn lastTurn, Turn secondToLastTurn) {
        int subtractFromLastTurn = 0;
        int subtractFromSecondToLastTurn = 0;

        for (Tile tile : lastTurn.getRack())
            subtractFromLastTurn -= tile.getValue();

        for (Tile tile : secondToLastTurn.getRack())
            subtractFromSecondToLastTurn -= tile.getValue();

        if (lastTurn.getRack().isEmpty())
            subtractFromLastTurn += subtractFromSecondToLastTurn;

        if (secondToLastTurn.getRack().isEmpty())
            subtractFromSecondToLastTurn += subtractFromLastTurn;

        Turn firstEndTurn = new Turn(lastTurn.getId() + 1,
                subtractFromSecondToLastTurn, secondToLastTurn.getUser(), TurnType.END);
        Turn secondEndTurn = new Turn(lastTurn.getId() + 2,
                subtractFromLastTurn, lastTurn.getUser(), TurnType.END);

        return new ArrayList<>(Arrays.asList(firstEndTurn, secondEndTurn));
    }

    public String getTurnWord(Field[][] gameBoard, ObservableList<Field> fieldsChanged) {
        if (fieldsChanged.isEmpty()) return null;
        this.gameBoard = gameBoard;
        this.fieldsChanged = fieldsChanged;
        verifyAndCalculate();
        if (!listOfFieldsWithWords.isEmpty())
            return getWordsFoundThisTurn().get(0);
        return null;
    }

    public Turn buildTurn(int newTurnId, User user, TurnType turnType) {
        return new Turn(newTurnId,
                getScore(),
                user,
                turnType,
                getTilesChangedThisTurn(),
                new ArrayList<>(getCurrentRack()));
    }

    public void verifyAndCalculate() {
        listOfFieldsWithWords.clear();
        if (!fieldsChanged.isEmpty()) {
            Character fixedAxis = verifyCurrentTurn();
            if (fixedAxis != null) {
                listOfFieldsWithWords = resolveWords(fixedAxis);
                score = calculateTotalScore(listOfFieldsWithWords);
                //debug();
            }
        }
    }

    private void debug() {
        System.out.println("score: " + score);
        listOfFieldsWithWords.forEach(fields -> {
            System.out.print("word: ");
            fields.stream().map(Field::getTile).forEach(System.out::print);
            System.out.print("\n");
        });
    }

    public int getScore() {
        return score;
    }

    public ArrayList<Tile> getTilesChangedThisTurn() {
        return fieldsChanged.stream()
                .map(Field::getTile)
                .collect(Collectors.toCollection(ArrayList<Tile>::new));
    }

    public ArrayList<String> getWordsFoundThisTurn() {
        ArrayList<String> words = new ArrayList<>();
        listOfFieldsWithWords.forEach(fields -> {
            StringBuilder newWord = new StringBuilder();
            fields.stream()
                    .map(Field::getTile)
                    .forEach(tile -> newWord.append(tile.toString()));
            words.add(newWord.toString());
        });
        return words;
    }

    public ObservableList<Tile> getCurrentRack() {
        return currentRack;
    }

    public void setCurrentRack(ObservableList<Tile> currentRack) {
        this.currentRack = currentRack;
    }

    public ObservableList<Field> getFieldsChanged() {
        return fieldsChanged;
    }

    public Field[][] getGameBoard() {
        return gameBoard;
    }

    public void addPlacedTile(Field field, Tile tile) {
        field.setTile(tile);
        tile.setX(field.getX());
        tile.setY(field.getY());
        fieldsChanged.add(field);
    }

    public void removePlacedTile(Field field) {
        field.getTile().clearCoordinates();
        field.setTile(null);
        fieldsChanged.remove(field);
    }

    /**
     * check if the current state of the game is a valid turn
     *
     * @return validTurn
     */
    public Character verifyCurrentTurn() {
        boolean validTurn = true;
        char fixedAxis = 0;

        /** if nothing is placed or the startTile is empty -> invalidate turn */
        if (fieldsChanged.size() == 0 || startFieldIsEmpty())
            validTurn = false;

        if (validTurn) {
            /** fetch first X and all X coords */
            int x = fieldsChanged.get(0).getX();
            ArrayList<Integer> xValues = fieldsChanged.stream()
                    .map(Field::getX)
                    .collect(Collectors.toCollection(ArrayList<Integer>::new));

            /** fetch first Y and All X coords */
            int y = fieldsChanged.get(0).getY();
            ArrayList<Integer> yValues = fieldsChanged.stream()
                    .map(Field::getY)
                    .collect(Collectors.toCollection(ArrayList<Integer>::new));

            /** check if placed on single axis, if not -> invalidate turn */
            if (Collections.frequency(xValues, x) != xValues.size()) {
                if (Collections.frequency(yValues, y) != yValues.size())
                    validTurn = false;  // Y and X are not equal for all tiles

                fixedAxis = 'y';        // Y is equal for all tiles
            } else
                fixedAxis = 'x';        // X is equal for all tiles

            /** check if there are gaps between placed letters */
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
            /**
             if only one tile has been placed at this point, check if it's a vertical word
             if not, and theres also no horizontal word, the turn is invalid, if there however is
             a horizontal word, flip the fixedAxis to horizontal
             */
            if (fieldsChanged.size() == 1 && findWords(checkColumn(fieldsChanged.get(0).getX())) == null) {
                if (findWords(new ArrayList<>(Arrays.asList(gameBoard[fieldsChanged.get(0).getY()]))) == null)
                    validTurn = false;
                else
                    fixedAxis = 'y';
            }
        }

        if (validTurn && !gameboardIsEmpty()) {
            boolean touches = false;
            /**
             all fields changed are already connected to each other at this point
             check if at least one of them connects to a tile from another turn
             */
            for (Field field : fieldsChanged) {
                int x = field.getX();
                int y = field.getY();
                // it touches if the field above has a tile not in fieldsChanged
                if (x > 0 && gameBoard[y][x - 1].getTile() != null && !fieldsChanged.contains(gameBoard[y][x - 1])) {
                    touches = true;
                    break;
                }

                // it touches if the field below has a tile not in fieldsChanged
                if (x < 14 && gameBoard[y][x + 1].getTile() != null && !fieldsChanged.contains(gameBoard[y][x + 1])) {
                    touches = true;
                    break;
                }

                // it touches if the field to the left has a tile not in fieldsChanged
                if (y > 0 && gameBoard[y - 1][x].getTile() != null && !fieldsChanged.contains(gameBoard[y - 1][x])) {
                    touches = true;
                    break;
                }

                // it touches if the field to the right has a tile not in fieldsChanged
                if (y < 14 && gameBoard[y + 1][x].getTile() != null && !fieldsChanged.contains(gameBoard[y + 1][x])) {
                    touches = true;
                    break;
                }
            }
            if (!touches)
                validTurn = false;
        }

        if (this.getCurrentRack() != null)
            System.out.println(validTurn);

        if (validTurn)
            return fixedAxis;
        else
            return null;
    }

    private boolean gameboardIsEmpty() {
        boolean isEmpty = true;
        for (int y = 0; y < gameBoard.length; y++) {
            for (int x = 0; x < gameBoard.length; x++) {
                if (gameBoard[y][x].getTile() != null && !fieldsChanged.contains(gameBoard[y][x])) {
                    isEmpty = false;
                    break;
                }
            }
        }
        return isEmpty;
    }

    private ArrayList<ArrayList<Field>> resolveWords(char fixedAxis) {
        ArrayList<ArrayList<Field>> wordsFound = new ArrayList<>();
        ArrayList<Field> word;
        switch (fixedAxis) {
            case 'x':
                int xPos = getFieldsChanged().get(0).getX();
                wordsFound.add(findWords(checkColumn(xPos)));
                for (Field field : getFieldsChanged()) {
                    word = findWords(new ArrayList<>(Arrays.asList(gameBoard[field.getY()])));
                    if (word != null) wordsFound.add(word);
                }
                break;
            case 'y':
                int yPos = getFieldsChanged().get(0).getY();
                wordsFound.add(findWords(new ArrayList<>(Arrays.asList(gameBoard[yPos]))));
                for (Field field : fieldsChanged) {
                    word = findWords(checkColumn(field.getX()));
                    if (word != null) wordsFound.add(word);
                }
                break;
            default:
                break;
        }
        return wordsFound;
    }

    private int calculateTotalScore(ArrayList<ArrayList<Field>> wordsFound) {
        int totalScore = 0;
        boolean otherAxis = false;
        for (ArrayList<Field> word : wordsFound) {
            int wordScore = calculateWordScore(word);

            if (wordsFound.size() == 2 && totalScore < wordScore)
                otherAxis = true;

            totalScore += wordScore;
        }

        if (otherAxis && fieldsChanged.size() == 1)
            Collections.swap(wordsFound, 0, 1);

        if (fieldsChanged.size() == 7)
            totalScore += 40;
        return totalScore;
    }

    private int calculateWordScore(ArrayList<Field> word) {
        int wordScore = 0;
        for (Field field : word) {
            if (fieldsChanged.contains(field) && (field.getFieldType() == FieldType.DL || field.getFieldType() == FieldType.TL)) {
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
            if (fieldsChanged.contains(field) && (field.getFieldType() == FieldType.DW || field.getFieldType() == FieldType.TW)) {
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
            if (field.getTile() != null)
                word.add(field);
            else {
                for (Field wordField : word)
                    if (fieldsChanged.contains(wordField) && word.size() > 1)
                        return word;
                word.clear();
            }
        }
        if (word.size() > 1) {
            return word;
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
        while (currentRack.size() < 7 && !playingPot.isEmpty())
            currentRack.add(playingPot.remove(new Random().nextInt(playingPot.size())));
    }

    public void setGameBoard(Field[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public ObservableList<Tile> getPot() {
        return pot;
    }

    public void setPot(ArrayList<Tile> pot) {
        currentRack.stream().filter(pot::contains).forEach(tile -> {
            System.out.println(tile.getCharacter() + "IS STILL IN POT");
        });
        this.pot = FXCollections.observableArrayList(pot);
    }
}

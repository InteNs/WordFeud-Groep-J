package models;

/**
 * Created by jeroen on 2-6-2016.
 */
import database.access.GameDAO;
import enumerations.BoardType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SWEN_test {


    private TurnBuilder turnbuilder;
    private GameDAO gameDAO;

    @Before
    public void setUp() throws Exception {
        turnbuilder = new TurnBuilder();
        gameDAO = new GameDAO();
        turnbuilder.setGameBoard(gameDAO.selectFieldsForBoard(BoardType.STANDARD));

    }

    private Tile t(char character) {
        switch (character) {
            case 'A': return new Tile('A', 1);
            case 'P': return new Tile('P', 4);
            case 'R': return new Tile('R', 1);
        }
        return null;
    }

    // Manier van Jeroen
    @Test
    public void testPath1_noTilesAdded(){
        turnbuilder.verifyAndCalculate();
        assertTrue("no tiles are added", turnbuilder.getTilesChangedThisTurn().size() == 0);
        assertTrue("score is 0", turnbuilder.getScore() == 0);
    }

    @Test
    public void testPath2_tilesNotPlacedOverStartTile(){
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][9],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][10],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][11],t('P'));

        assertTrue("tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNull("turn is not valid", turnbuilder.verifyCurrentTurn());
        assertTrue("score is 0", turnbuilder.getScore() == 0);
    }

    @Test
    public void testPath3_wordNotInOneLine(){
        // A valid word is not on 1 axis
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][7],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][8],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][9],t('P'));

        assertTrue("tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNull("turn is not valid", turnbuilder.verifyCurrentTurn());
        assertTrue("score is 0", turnbuilder.getScore() == 0);

    }

    @Test
    public void testPath4_emptySpaceInXAxis(){
        // A word is placed with a empty space on the x axis.
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][7],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][8],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][10],t('P'));

        assertTrue("tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNull("turn is not valid", turnbuilder.verifyCurrentTurn());
        assertTrue("score is 0", turnbuilder.getScore() == 0);

    }

    @Test
    public void testPath5_invalidWord(){
        // Tiles are placed on correct spots, but word is incorrect
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][7],t('A'));

        assertTrue("tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNull("turn is not valid", turnbuilder.verifyCurrentTurn());
        assertTrue("score is 0", turnbuilder.getScore() == 0);
    }

    @Test
    public void testPath6_wordNotInOneLine(){
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][8],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[9][8],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[10][9],t('P'));

        assertTrue("tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNull("turn is not valid", turnbuilder.verifyCurrentTurn());
        assertTrue("score is 0", turnbuilder.getScore() == 0);

        printGameBoard();
    }

    @Test
    public void testPath9_invalidWord(){
        // Tiles are placed on correct spots, but word is incorrect
        turnbuilder.getGameBoard()[7][7].setTile(t('A'));
        turnbuilder.getGameBoard()[7][8].setTile(t('A'));
        turnbuilder.getGameBoard()[7][9].setTile(t('P'));

        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][9],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][10],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][11],t('P'));


        assertTrue("tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull(turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("score is 0", turnbuilder.getScore() == 12);

        printGameBoard();
    }

    @Test
    public void testPath10_oneLetterAddedYaxisOnDoubleLetterTile(){
        turnbuilder.getGameBoard()[7][7].setTile(t('P'));
        turnbuilder.getGameBoard()[8][7].setTile(t('A'));
        turnbuilder.getGameBoard()[9][7].setTile(t('A'));
        turnbuilder.getGameBoard()[10][7].setTile(t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[10][8], t('A'));

        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 3", turnbuilder.getScore() == 3);

        printGameBoard();
    }

    @Test
    public void testPath11_allSevenLettersAddedonXAxis(){
        turnbuilder.getGameBoard()[7][7].setTile(t('R'));
        turnbuilder.getGameBoard()[7][11].setTile(t('R'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][6], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][5], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][8], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][10], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][4], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][12], t('A'));

        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 49", turnbuilder.getScore() == 49);

        printGameBoard();

    }

    @Test
    public void testPath12_allSevenLettersAddedYaxisOnTripleLetterTile(){
        turnbuilder.getGameBoard()[7][7].setTile(t('P'));
        turnbuilder.getGameBoard()[8][7].setTile(t('A'));
        turnbuilder.getGameBoard()[9][7].setTile(t('A'));
        turnbuilder.getGameBoard()[9][8].setTile(t('A'));
        turnbuilder.getGameBoard()[9][9].setTile(t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[6][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[10][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[11][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[12][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[13][9], t('A'));


        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 50", turnbuilder.getScore() == 50);

        printGameBoard();
    }

    @Test
    public void testPath13_allSevenLettersOnXaxisWithDoubleWord(){
        turnbuilder.getGameBoard()[7][7].setTile(t('R'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][6], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][5], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][8], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][10], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][11], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][12], t('A'));

        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 56", turnbuilder.getScore() == 56);

        printGameBoard();
    }

    @Test
    public void testPath14_allSevenLettersaddedonXAxisWithTripleWord(){
        turnbuilder.getGameBoard()[7][7].setTile(t('R'));
        turnbuilder.getGameBoard()[8][7].setTile(t('R'));
        turnbuilder.getGameBoard()[9][7].setTile(t('R'));
        turnbuilder.getGameBoard()[10][7].setTile(t('R'));
        turnbuilder.getGameBoard()[11][7].setTile(t('R'));
        turnbuilder.getGameBoard()[12][7].setTile(t('R'));
        turnbuilder.getGameBoard()[13][7].setTile(t('R'));
        turnbuilder.getGameBoard()[14][7].setTile(t('R'));

        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[14][5], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[14][6], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[14][8], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[14][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[14][10], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[14][11], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[14][12], t('A'));

        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 64", turnbuilder.getScore() == 64);

        printGameBoard();
    }


    @Test
    public void testPath15_LettersOnXaxisWithTripleLetter(){
        turnbuilder.getGameBoard()[7][7].setTile(t('R'));
        turnbuilder.getGameBoard()[6][7].setTile(t('R'));
        turnbuilder.getGameBoard()[5][7].setTile(t('R'));
        turnbuilder.getGameBoard()[5][8].setTile(t('R'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[5][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[5][10], t('A'));


        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 6", turnbuilder.getScore() == 6);

        printGameBoard();
    }

    @Test
    public void testPath16_LettersOnXaxisWithDoubleWord(){
        turnbuilder.getGameBoard()[7][7].setTile(t('R'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][6], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][5], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][8], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][9], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][10], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[7][11], t('A'));

        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 14", turnbuilder.getScore() == 14);

        printGameBoard();
    }


    @Test
    public void testPath17_LettersOnYaxisWithTripleLetterTripleWord(){
        turnbuilder.getGameBoard()[7][7].setTile(t('R'));
        turnbuilder.getGameBoard()[6][7].setTile(t('R'));
        turnbuilder.getGameBoard()[5][7].setTile(t('R'));
        turnbuilder.getGameBoard()[5][8].setTile(t('R'));
        turnbuilder.getGameBoard()[5][9].setTile(t('R'));
        turnbuilder.getGameBoard()[5][10].setTile(t('R'));
        turnbuilder.getGameBoard()[5][11].setTile(t('R'));
        turnbuilder.getGameBoard()[5][12].setTile(t('R'));
        turnbuilder.getGameBoard()[5][13].setTile(t('R'));
        turnbuilder.getGameBoard()[5][13].setTile(t('R'));
        turnbuilder.getGameBoard()[5][14].setTile(t('R'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[4][14], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[3][14], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[2][14], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[1][14], t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[0][14], t('A'));



        assertTrue("Tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNotNull("Turn is valid", turnbuilder.verifyCurrentTurn());
        turnbuilder.verifyAndCalculate();
        assertTrue("Score is 24", turnbuilder.getScore() == 24);

        printGameBoard();
    }

    private void printGameBoard() {
        for(int i = 0; i < turnbuilder.getGameBoard().length; i ++){
            for(int x = 0; x < turnbuilder.getGameBoard().length; x ++) {
                if(x == (turnbuilder.getGameBoard().length -1)){
                    System.out.println(turnbuilder.getGameBoard()[ i ][ x ].toString());
                } else {
                    System.out.print(turnbuilder.getGameBoard()[ i ][ x ].toString());
                }

            }
        }
    }
}

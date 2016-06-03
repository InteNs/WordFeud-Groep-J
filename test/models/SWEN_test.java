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

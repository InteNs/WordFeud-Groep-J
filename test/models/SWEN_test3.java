package models;

/**
 * Created by jeroen on 2-6-2016.
 */

import database.access.GameDAO;
import enumerations.BoardType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SWEN_test3 {


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

    @Test
    public void wordNotInOneLine(){
        // A valid word is placed wrong
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][8],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[8][9],t('A'));
        turnbuilder.addPlacedTile(turnbuilder.getGameBoard()[9][10],t('P'));

        assertTrue("tiles are added", turnbuilder.getTilesChangedThisTurn().size() > 0);
        assertNull("turn is not valid", turnbuilder.verifyCurrentTurn());
        assertTrue("score is 0", turnbuilder.getScore() == 0);

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

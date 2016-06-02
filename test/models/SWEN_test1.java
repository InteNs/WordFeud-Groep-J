package models;

/**
 * Created by jeroen on 2-6-2016.
 */
import database.access.GameDAO;
import enumerations.BoardType;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class SWEN_test1 {


    private TurnBuilder turnbuilder;
    private GameDAO gameDAO;

    @Before
    public void setUp() throws Exception {
        turnbuilder = new TurnBuilder();
        gameDAO = new GameDAO();
        turnbuilder.setGameBoard(gameDAO.selectFieldsForBoard(BoardType.STANDARD));

        turnbuilder.getGameBoard()[7][7].setTile(t('A'));
        turnbuilder.getGameBoard()[7][8].setTile(t('A'));
        turnbuilder.getGameBoard()[7][9].setTile(t('P'));
    }

    private Tile t(char character) {
        switch (character) {
            case 'A': return new Tile('A', 1);
            case 'P': return new Tile('P', 4);
        }
        return null;
    }

    @Test
    public void noTilesAdded(){
        turnbuilder.verifyAndCalculate();
        assertTrue("no tiles are added", turnbuilder.getTilesChangedThisTurn().size() == 0);
        assertTrue("score is 0", turnbuilder.getScore() == 0);
    }
}
